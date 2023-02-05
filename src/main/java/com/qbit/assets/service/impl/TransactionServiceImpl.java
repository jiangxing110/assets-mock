package com.qbit.assets.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.common.enums.BalanceOperationTypeEnum;
import com.qbit.assets.common.enums.TransactionDisplayStatusEnum;
import com.qbit.assets.common.enums.TransactionStatusEnum;
import com.qbit.assets.common.enums.TransactionTypeEnum;
import com.qbit.assets.common.error.CustomException;
import com.qbit.assets.common.utils.RedisLockUtil;
import com.qbit.assets.domain.dto.BalanceChangeDTO;
import com.qbit.assets.domain.entity.Balance;
import com.qbit.assets.domain.entity.BalanceTransaction;
import com.qbit.assets.domain.entity.Transaction;
import com.qbit.assets.mapper.TransactionMapper;
import com.qbit.assets.service.BalanceService;
import com.qbit.assets.service.BalanceTransactionService;
import com.qbit.assets.service.TransactionService;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author martin
 * @since 2023-02-05
 */
@Service
public class TransactionServiceImpl extends ServiceImpl<TransactionMapper, Transaction> implements TransactionService {
    @Resource
    private BalanceService balanceService;

    @Resource
    private TransactionMapper transactionMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private BalanceTransactionService balanceTransactionService;

    @Resource
    private RedisLockUtil redisLockUtil;

    /**
     * 更新订单
     *
     * @param transactionId
     * @param status
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public Transaction updateBalanceByBalanceTransactionIdV2(String transactionId, TransactionStatusEnum status) {
        var Transaction = new Transaction();
        RLock rLock = redisLockUtil.reentrantLock("qbit:3:redisLock:transactionId:" + transactionId);
        try {
            Transaction = this.updateBalanceByBalanceTransactionIdV2Action(transactionId, status);
        } catch (Exception e) {
            throw e;
        } finally {
            redisLockUtil.unLock(rLock);
        }
        return Transaction;
    }

    /**
     * @param transactionId
     * @param status
     * @return
     */
    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public Transaction updateBalanceByBalanceTransactionIdV2Action(String transactionId, TransactionStatusEnum status) {
        // 查看最终态的是否有(如果有就拦截)
        var transaction = this.checkFinalStateBalanceTransactionStatus(transactionId);

        String[] balanceIds = {transaction.getSenderBalanceId(), transaction.getRecipientBalanceId()};
        var balanceWrapper = new LambdaQueryWrapper<Balance>();
        balanceWrapper.in(Balance::getId, balanceIds);
        balanceWrapper.orderByAsc(Balance::getCreateTime);
        balanceWrapper.orderByDesc(Balance::getUpdateTime);
        var balanceList = balanceService.list(balanceWrapper);
        // 2022-09-27 梁伟 增加账户互转的锁，千万不要改位置
        for (Balance balance : balanceList) {
            balanceService.findBalanceByIdForUpdate(balance.getId());
        }

        // 单钱包加钱更新
        if (transaction.getBalanceOperationType().equals(BalanceOperationTypeEnum.SingleBalanceAdd.toString())) {
            if (status == TransactionStatusEnum.Closed) {
                return this.singleBalanceAddAmountPendingToClosedV2(transactionId);
            } else if (status == TransactionStatusEnum.Fail) {
                return this.singleBalanceAddAmountPendingToFailV2(transactionId);
            }
        }

        // 单钱包减钱更新
        if (transaction.getBalanceOperationType().equals(BalanceOperationTypeEnum.SingleBalanceSub.toString())) {
            if (status == TransactionStatusEnum.Closed) {
                return this.singleBalanceSubAmountPendingToClosedV2(transactionId);
            } else if (status == TransactionStatusEnum.Fail) {
                return this.singleBalanceSubAmountPendingToFailV2(transactionId);
            }
        }

        // 内部转账
        if (transaction.getBalanceOperationType().equals(BalanceOperationTypeEnum.SystemInternalTransfer.toString())) {
            if (status == TransactionStatusEnum.Closed) {
                return this.internalBalanceTransferPendingToClosedV2(transactionId);
            } else if (status == TransactionStatusEnum.Fail) {
                return internalBalanceTransferPendingToFailV2(transactionId);
            }
        }
        throw new CustomException("'暂时不支持该场景'", HttpStatus.BAD_REQUEST);
    }


    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public Transaction checkFinalStateBalanceTransactionStatus(String transactionId) {
        var transactionObj = transactionMapper.selectById(transactionId);
        if (transactionObj == null) {
            throw new CustomException("id is Error", HttpStatus.BAD_REQUEST);
        }
        if (transactionObj.getStatus() != TransactionStatusEnum.Pending) {
            throw new CustomException("订单已处理", HttpStatus.BAD_REQUEST);
        }

        var wrapper = new LambdaQueryWrapper<BalanceTransaction>();
        wrapper.eq(BalanceTransaction::getSourceId, transactionId);
        wrapper.orderByDesc(BalanceTransaction::getCreateTime);
        var balanceTransactionDb = balanceTransactionService.getOne(wrapper);
        if (balanceTransactionDb == null) {
            throw new CustomException("异常的订单", HttpStatus.BAD_REQUEST);
        }
        if (balanceTransactionDb.getType() != TransactionStatusEnum.Pending) {
            throw new CustomException("订单已处理", HttpStatus.BAD_REQUEST);
        }

        return transactionObj;
    }

    /**
     * 单钱包加钱后的pending状态（注意，必须配套使用）
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public Transaction singleBalanceAddAmountToPendingV2(BalanceChangeDTO params) {
        Transaction transaction = createAddTransaction(params);
        var balanceObj = balanceService.getById(transaction.getRecipientBalanceId());
        if (balanceObj == null || !balanceObj.getCurrency().equals(transaction.getSenderCurrency())) {
            throw new CustomException("id is Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 单钱包加钱
        var cost = params.getCost();
        var fee = params.getFee() == null ? BigDecimal.valueOf(0.00) : params.getFee();
        var amount = cost.subtract(fee);
        balanceService.balanceAddPendingAmount(params.getBalanceId(), amount);
        // 保存其他信息
        var currentSubBalanceObj = balanceService.getById(balanceObj.getId());
        var balanceTransaction = balanceTransactionService.createBalanceTransaction(transaction);
        var sqlStr = MessageFormat.format("update balance SET pending = pending + {0}, version = version + 1 WHERE id = {1}", amount, balanceObj.getId());
        String[] sqlList = {sqlStr};
        balanceTransaction.setSqlExecuteList(sqlList);
        this.balanceTransactionService.saveBalanceToBalanceTx(balanceTransaction, null, currentSubBalanceObj);
        return transaction;
    }

    /**
     * 单钱包加钱更新成closed
     *
     * @param transactionId
     * @return
     */
    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public Transaction singleBalanceAddAmountPendingToClosedV2(String transactionId) {
        var wrapper = new LambdaQueryWrapper<Transaction>();
        wrapper.eq(Transaction::getId, transactionId);
        wrapper.eq(Transaction::getStatus, TransactionStatusEnum.Pending);
        var transaction = transactionMapper.selectOne(wrapper);
        if (transaction == null) {
            throw new CustomException("id is Error", HttpStatus.BAD_REQUEST);
        }

        transaction.setStatus(TransactionStatusEnum.Closed);
        int updateTransaction = transactionMapper.updateById(transaction);
        if (updateTransaction <= 0) {
            throw new CustomException("订单状态更新失败");
        }

        if (!transaction.getBalanceOperationType().equals(BalanceOperationTypeEnum.SingleBalanceAdd.toString())) {
            throw new CustomException("'暂时不支持该场景'", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        var balanceObj = balanceService.getById(transaction.getRecipientBalanceId());
        if (balanceObj == null || !balanceObj.getCurrency().equals(transaction.getSenderCurrency())) {
            throw new CustomException("id is Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 单钱包加钱更新（pending:-，Available:+）
        var recipientCost = transaction.getRecipientCost();
        var recipientFee = transaction.getRecipientFee();
        var amount = recipientCost.subtract(recipientFee);
        // pending 减少
        balanceService.balanceSubPendingAmount(balanceObj.getId(), amount);
        // Available 增加
        balanceService.balanceAddAvailableAmount(balanceObj.getId(), amount);
        // 保存其他信息
        var currentAddBalanceObj = balanceService.getById(balanceObj.getId());
        var balanceTransaction = balanceTransactionService.createBalanceTransaction(transaction);
        var pendingSubSqlStr = MessageFormat.format("update balance SET pending = pending - {0}, version = version + 1 WHERE id = {1} and pending >= {2}", amount, balanceObj.getId(), amount);
        String[] sqlList = {pendingSubSqlStr};
        balanceTransaction.setSqlExecuteList(sqlList);
        this.balanceTransactionService.saveBalanceToBalanceTx(balanceTransaction, currentAddBalanceObj, null);
        return transaction;
    }

    /**
     * 单钱包加钱更新成Fail
     *
     * @param transactionId
     * @return
     */
    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public Transaction singleBalanceAddAmountPendingToFailV2(String transactionId) {
        var wrapper = new LambdaQueryWrapper<Transaction>();
        wrapper.eq(Transaction::getId, transactionId);
        wrapper.eq(Transaction::getStatus, TransactionStatusEnum.Pending);
        var transaction = transactionMapper.selectOne(wrapper);
        if (transaction == null) {
            throw new CustomException("id is Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        transaction.setStatus(TransactionStatusEnum.Fail);
        int updateTransactionCount = transactionMapper.updateById(transaction);
        if (updateTransactionCount <= 0) {
            throw new CustomException("订单状态更新失败");
        }

        if (!BalanceOperationTypeEnum.SingleBalanceAdd.value().equals(transaction.getBalanceOperationType())) {

            throw new CustomException("'暂时不支持该场景'", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        var balanceObj = balanceService.getById(transaction.getRecipientBalanceId());
        if (balanceObj == null || !balanceObj.getCurrency().equals(transaction.getSenderCurrency())) {
            throw new CustomException("id is Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 单钱包加钱更新（pending:-）
        var recipientCost = transaction.getRecipientCost();
        var recipientFee = transaction.getRecipientFee();
        var amount = recipientCost.subtract(recipientFee);

        // pending 减少
        balanceService.balanceSubPendingAmount(balanceObj.getId(), amount);

        // 保存其他信息
        var currentAddBalanceObj = balanceService.getById(balanceObj.getId());
        var balanceTransaction = balanceTransactionService.createBalanceTransaction(transaction);
        var pendingSubSqlStr = MessageFormat.format("update balance SET pending = pending - {0}, version = version + 1 WHERE id = {1} and pending >= {2}", amount, balanceObj.getId(), amount);
        var availableAddSqlStr = MessageFormat.format("update balance SET available = available + {0}, version = version + 1 WHERE id = {1}", amount, balanceObj.getId());
        String[] sqlList = {pendingSubSqlStr, availableAddSqlStr};
        balanceTransaction.setSqlExecuteList(sqlList);
        this.balanceTransactionService.saveBalanceToBalanceTx(balanceTransaction, currentAddBalanceObj, null);
        return transaction;
    }


    /**
     * 单钱包减钱后的pending状态（注意，必须配套使用）
     *
     * @param params
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public Transaction singleBalanceSubAmountToPendingV2(BalanceChangeDTO params) {
        var transaction = createSubTransaction(params);
        var balanceObj = balanceService.getById(transaction.getSenderBalanceId());
        if (balanceObj == null || !balanceObj.getCurrency().equals(transaction.getSenderCurrency())) {
            throw new CustomException("id is Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // 单钱包加钱
        var cost = params.getCost();
        var fee = params.getFee();
        var amount = cost.add(fee);

        // 减Available，加pending
        balanceService.balanceSubAvailableAmount(params.getBalanceId(), amount);
        balanceService.balanceAddPendingAmount(params.getBalanceId(), amount);

        // 保存其他信息
        var currentSubBalanceObj = balanceService.getById(balanceObj.getId());
        var balanceTransaction = balanceTransactionService.createBalanceTransaction(transaction);
        var pendingSubSqlStr = MessageFormat.format("update balance SET pending = pending - {0}, version = version + 1 WHERE id = {1} and pending >= {2}", amount, balanceObj.getId(), amount);
        var availableAddSqlStr = MessageFormat.format("update balance SET available = available + {0}, version = version + 1 WHERE id = {1}", amount, balanceObj.getId());
        String[] sqlList = {pendingSubSqlStr, availableAddSqlStr};
        balanceTransaction.setSqlExecuteList(sqlList);
        this.balanceTransactionService.saveBalanceToBalanceTx(balanceTransaction, currentSubBalanceObj, null);
        return transaction;
    }

    /**
     * 单钱包的减钱更新成成功
     */
    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public Transaction singleBalanceSubAmountPendingToClosedV2(String transactionId) {
        var wrapper = new LambdaQueryWrapper<Transaction>();
        wrapper.eq(Transaction::getId, transactionId);
        wrapper.eq(Transaction::getStatus, TransactionStatusEnum.Pending);
        var transaction = transactionMapper.selectOne(wrapper);
        if (transaction == null) {
            throw new CustomException("id is Error", HttpStatus.BAD_REQUEST);
        }

        transaction.setStatus(TransactionStatusEnum.Closed);
        transactionMapper.updateById(transaction);


        if (!BalanceOperationTypeEnum.SingleBalanceSub.toString().equals(transaction.getBalanceOperationType())) {
            throw new CustomException("'暂时不支持该场景'", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        var balanceObj = balanceService.getById(transaction.getSenderBalanceId());
        if (balanceObj == null || !balanceObj.getCurrency().equals(transaction.getSenderCurrency())) {
            throw new CustomException("id is Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 单钱包加钱更新（pending:-）
        var senderCost = transaction.getSenderCost();
        var senderFee = transaction.getSenderFee();
        var amount = senderCost.add(senderFee);
        // pending 减少
        balanceService.balanceSubPendingAmount(balanceObj.getId(), amount);

        // 保存其他信息
        var currentSubBalanceObj = balanceService.getById(balanceObj.getId());
        var balanceTransaction = balanceTransactionService.createBalanceTransaction(transaction);
        var pendingAddSqlStr = MessageFormat.format("update balance SET pending = pending + {0}, version = version + 1 WHERE id = {1}", amount, balanceObj.getId());
        var availableSubSqlStr = MessageFormat.format("update balance SET available = available - {0}, version = version + 1 WHERE id = {1} and available >= #{2}", amount, balanceObj.getId(), amount);
        String[] sqlList = {pendingAddSqlStr, availableSubSqlStr};
        balanceTransaction.setSqlExecuteList(sqlList);
        this.balanceTransactionService.saveBalanceToBalanceTx(balanceTransaction, currentSubBalanceObj, null);
        return transaction;
    }

    /**
     * 单钱包的减钱 更新成失败
     *
     * @param transactionId
     * @returns
     */
    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public Transaction singleBalanceSubAmountPendingToFailV2(String transactionId) {
        var wrapper = new LambdaQueryWrapper<Transaction>();
        wrapper.eq(Transaction::getId, transactionId);
        wrapper.eq(Transaction::getStatus, TransactionStatusEnum.Pending);
        var transaction = transactionMapper.selectOne(wrapper);
        if (transaction == null) {
            throw new CustomException("id is Error", HttpStatus.BAD_REQUEST);
        }

        transaction.setStatus(TransactionStatusEnum.Fail);
        transactionMapper.updateById(transaction);


        if (!transaction.getBalanceOperationType().equals(BalanceOperationTypeEnum.SingleBalanceSub.toString())) {
            throw new CustomException("'暂时不支持该场景'", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        var balanceObj = balanceService.getById(transaction.getSenderBalanceId());
        if (balanceObj == null || !balanceObj.getCurrency().equals(transaction.getSenderCurrency())) {
            throw new CustomException("id is Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 单钱包加钱更新（pending:-）
        var senderCost = transaction.getSenderCost();
        var senderFee = transaction.getSenderFee();
        var amount = senderCost.add(senderFee);
        // pending 减少, Available 增加
        balanceService.balanceSubPendingAmount(balanceObj.getId(), amount);
        balanceService.balanceAddAvailableAmount(balanceObj.getId(), amount);
        // 保存其他信息
        var currentSubBalanceObj = balanceService.getById(balanceObj.getId());
        var balanceTransaction = balanceTransactionService.createBalanceTransaction(transaction);
        var pendingSubSqlStr = MessageFormat.format("update balance SET pending = pending - {0}, version = version + 1 WHERE id = {1} and pending >= {2}", amount, balanceObj.getId(), amount);
        String[] sqlList = {pendingSubSqlStr};
        balanceTransaction.setSqlExecuteList(sqlList);
        this.balanceTransactionService.saveBalanceToBalanceTx(balanceTransaction, currentSubBalanceObj, null);
        return transaction;
    }

    /**
     * 内部转账pending v2
     *
     * @param subBalanceParams
     * @param addBalanceParams
     * @returns
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public synchronized Transaction internalBalanceTransferPendingV2(BalanceChangeDTO subBalanceParams, BalanceChangeDTO addBalanceParams) {
        Transaction transaction = null;
        var fromBalanceId = subBalanceParams.getBalanceId();
        var toBalanceId = addBalanceParams.getBalanceId();

//        var fromBalanceLock = redissonClient.getLock(fromBalanceId);
//        var toBalanceLock = redissonClient.getLock(toBalanceId);
//        var multiLock = redissonClient.getMultiLock(fromBalanceLock, toBalanceLock);
        try {
            // 同时加锁：lock1 lock2 所有的锁都上锁成功才算成功。
//            multiLock.lock();
            // 尝试加锁，最多等待30秒，上锁以后10秒自动解锁
//            boolean res = multiLock.tryLock(30, 10, TimeUnit.SECONDS);
//            if (!res) {
//                throw new CustomException("lock fail");
//            }
            // 内部转账
            transaction = this.internalBalanceTransferPendingV2Action(subBalanceParams, addBalanceParams);
            return transaction;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }/* finally {
            multiLock.unlock();
        }*/
    }

    /**
     * 冻结可用
     *
     * @param params
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public Transaction unFrozenAvailableAmount(BalanceChangeDTO params) {
        Transaction transaction = createAddTransaction(params);
        var balanceObj = balanceService.getById(transaction.getRecipientBalanceId());
        if (balanceObj == null || !balanceObj.getCurrency().equals(transaction.getSenderCurrency())) {
            throw new CustomException("id is Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 单钱包加钱
        var cost = params.getCost();
        var fee = params.getFee() == null ? BigDecimal.valueOf(0.00) : params.getFee();
        var amount = cost.subtract(fee);

        // 减frozen，加 Available
        balanceService.balanceSubFrozenAmount(params.getBalanceId(), amount);
        balanceService.balanceAddAvailableAmount(params.getBalanceId(), amount);

        // 保存其他信息
        LambdaUpdateWrapper<Transaction> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Transaction::getId, transaction.getId())
                .set(Transaction::getType, TransactionTypeEnum.UnFrozen)
                .set(Transaction::getStatus, TransactionStatusEnum.Closed)
                .set(Transaction::getDisplayStatus, TransactionDisplayStatusEnum.Closed);
        this.update(updateWrapper);

        var currentSubBalanceObj = balanceService.getById(balanceObj.getId());
        var balanceTransaction = balanceTransactionService.createBalanceTransaction(transaction);
        var pendingSubSqlStr = MessageFormat.format("update balance SET frozen = frozen - {0}, version = version + 1 WHERE id = {1} and frozen >= {2}", amount, balanceObj.getId(), amount);
        var availableAddSqlStr = MessageFormat.format("update balance SET available = available + {0}, version = version + 1 WHERE id = {1} ", amount, balanceObj.getId());
        String[] sqlList = {pendingSubSqlStr, availableAddSqlStr};
        balanceTransaction.setSqlExecuteList(sqlList);
        this.balanceTransactionService.saveBalanceToBalanceTx(balanceTransaction, currentSubBalanceObj, null);
        return transaction;
    }

    /**
     * 冻结可用
     *
     * @param params
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public Transaction frozenAvailableAmount(BalanceChangeDTO params) {
        var transaction = createSubTransaction(params);
        var balanceObj = balanceService.getById(transaction.getSenderBalanceId());
        if (balanceObj == null || !balanceObj.getCurrency().equals(transaction.getSenderCurrency())) {
            throw new CustomException("id is Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // 单钱包冻结可用
        var cost = params.getCost();
        var fee = params.getFee();
        var amount = cost.add(fee);

        // 减Available，加frozen
        balanceService.balanceSubAvailableAmount(params.getBalanceId(), amount);
        balanceService.balanceAddFrozenAmount(params.getBalanceId(), amount);

        // 保存其他信息
        LambdaUpdateWrapper<Transaction> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Transaction::getId, transaction.getId())
                .set(Transaction::getType, TransactionTypeEnum.Frozen)
                .set(Transaction::getStatus, TransactionStatusEnum.Closed)
                .set(Transaction::getDisplayStatus, TransactionDisplayStatusEnum.Closed);
        this.update(updateWrapper);

        var currentSubBalanceObj = balanceService.getById(balanceObj.getId());
        var balanceTransaction = balanceTransactionService.createBalanceTransaction(transaction);
        var pendingSubSqlStr = MessageFormat.format("update balance SET frozen = frozen + {0}, version = version + 1 WHERE id = {1}", amount, balanceObj.getId(), amount);
        var availableAddSqlStr = MessageFormat.format("update balance SET available = available - {0}, version = version + 1 WHERE id = {1} and available >= {2}", amount, balanceObj.getId());
        String[] sqlList = {pendingSubSqlStr, availableAddSqlStr};
        balanceTransaction.setSqlExecuteList(sqlList);
        this.balanceTransactionService.saveBalanceToBalanceTx(balanceTransaction, currentSubBalanceObj, null);
        return transaction;
    }

    /**
     * 内部转账金额变动
     *
     * @param subBalanceParams
     * @param addBalanceParams
     * @return
     */
    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public Transaction internalBalanceTransferPendingV2Action(BalanceChangeDTO subBalanceParams, BalanceChangeDTO addBalanceParams) {
        var transaction = createInternalBalanceTransferTx(subBalanceParams, addBalanceParams);
        // senderBalance 需要减少的钱（cost + fee）
        var senderCost = transaction.getSenderCost();
        var senderFee = transaction.getSenderFee();
        var senderAmountAddFee = senderCost.add(senderFee);

        // recipientAmount 需要增加的钱（cost - fee）
        var recipientCost = transaction.getRecipientCost();
        var recipientFee = transaction.getRecipientFee();
        var recipientAmountSubFee = recipientCost.subtract(recipientFee);

        var senderBalanceObj = balanceService.getById(transaction.getSenderBalanceId());
        if (senderBalanceObj == null || !senderBalanceObj.getCurrency().equals(transaction.getSenderCurrency())) {
            throw new CustomException("id is Error", HttpStatus.BAD_REQUEST);
        }

        var recipientBalanceObj = balanceService.getById(transaction.getRecipientBalanceId());
        if (recipientBalanceObj == null || !recipientBalanceObj.getCurrency().equals(transaction.getRecipientCurrency())) {
            throw new CustomException("id is Error", HttpStatus.BAD_REQUEST);
        }

        String[] balanceIds = {senderBalanceObj.getId(), recipientBalanceObj.getId()};
        var balanceWrapper = new LambdaQueryWrapper<Balance>();
        balanceWrapper.in(Balance::getId, balanceIds);
        balanceWrapper.orderByAsc(Balance::getCreateTime);
        balanceWrapper.orderByDesc(Balance::getUpdateTime);
        var balanceList = balanceService.list(balanceWrapper);
        // 2022-09-27 梁伟 增加账户互转的锁，千万不要改位置
        for (Balance balance : balanceList) {
            balanceService.findBalanceByIdForUpdate(balance.getId());
        }

        // senderBalance pending+,Available-
        balanceService.balanceAddPendingAmount(senderBalanceObj.getId(), senderAmountAddFee);
        balanceService.balanceSubAvailableAmount(senderBalanceObj.getId(), senderAmountAddFee);
        // recipient pending+
        balanceService.balanceAddPendingAmount(recipientBalanceObj.getId(), recipientAmountSubFee);

        // 保存其他信息
        var senderBalance = balanceService.getById(senderBalanceObj.getId());
        var recipientBalance = balanceService.getById(recipientBalanceObj.getId());
        var balanceTransaction = balanceTransactionService.createBalanceTransaction(transaction);
        var senderPendingAddSqlStr = MessageFormat.format("update balance SET pending = pending - {0}, version = version + 1 WHERE id = {1} and pending >= {2}", senderAmountAddFee, senderBalanceObj.getId(), senderAmountAddFee);
        var senderAvailableSubSqlStr = MessageFormat.format("update balance SET available = available + {0}, version = version + 1 WHERE id = {1}", senderAmountAddFee, senderBalanceObj.getId());
        var recipientPendingAddSqlStr = MessageFormat.format("update balance SET pending = pending + {0}, version = version + 1 WHERE id = {1}", recipientAmountSubFee, recipientBalanceObj.getId());
        String[] sqlList = {senderPendingAddSqlStr, senderAvailableSubSqlStr, recipientPendingAddSqlStr};
        balanceTransaction.setSqlExecuteList(sqlList);
        this.balanceTransactionService.saveBalanceToBalanceTx(balanceTransaction, senderBalance, recipientBalance);
        return transaction;
    }

    /**
     * 内部转账更新成成功
     *
     * @param transactionId
     */
    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public Transaction internalBalanceTransferPendingToClosedV2(String transactionId) {
        var wrapper = new LambdaQueryWrapper<Transaction>();
        wrapper.eq(Transaction::getId, transactionId);
        wrapper.eq(Transaction::getBalanceOperationType, BalanceOperationTypeEnum.SystemInternalTransfer);
        wrapper.eq(Transaction::getStatus, TransactionStatusEnum.Pending);
        var transaction = transactionMapper.selectOne(wrapper);
        if (transaction == null) {
            throw new CustomException("id is Error", HttpStatus.BAD_REQUEST);
        }

        transaction.setStatus(TransactionStatusEnum.Closed);
        transactionMapper.updateById(transaction);

        //var senderBalanceObj = balanceService.getById(transaction.getSenderBalanceId());
        var senderBalanceObj = balanceService.getById(transaction.getSenderBalanceId());
        if (senderBalanceObj == null || !senderBalanceObj.getCurrency().equals(transaction.getSenderCurrency())) {
            throw new CustomException("id is Error", HttpStatus.BAD_REQUEST);
        }

        var recipientBalanceObj = balanceService.getById(transaction.getRecipientBalanceId());
        if (recipientBalanceObj == null || !recipientBalanceObj.getCurrency().equals(transaction.getRecipientCurrency())) {
            throw new CustomException("id is Error", HttpStatus.BAD_REQUEST);
        }


        var senderCost = transaction.getSenderCost();
        var senderFee = transaction.getSenderFee();
        var senderAmount = senderCost.add(senderFee);

        var recipientCost = transaction.getRecipientCost();
        var recipientFee = transaction.getRecipientFee();

        recipientFee = recipientFee == null ? BigDecimal.ZERO : recipientFee;
        var recipientAmount = recipientCost.subtract(recipientFee);

        // senderBalance pending-
        balanceService.balanceSubPendingAmount(senderBalanceObj.getId(), senderAmount);

        // recipient pending-, Available+
        balanceService.balanceSubPendingAmount(recipientBalanceObj.getId(), recipientAmount);
        balanceService.balanceAddAvailableAmount(recipientBalanceObj.getId(), recipientAmount);


        // 保存其他信息
        var senderBalance = balanceService.getById(senderBalanceObj.getId());
        var recipientBalance = balanceService.getById(recipientBalanceObj.getId());
        var balanceTransaction = balanceTransactionService.createBalanceTransaction(transaction);
        var senderPendingAddSqlStr = MessageFormat.format("update balance SET pending = pending + {0}, version = version + 1 WHERE id = {1}", senderAmount, senderBalanceObj.getId());
        var senderAvailableSubSqlStr = MessageFormat.format("update balance SET available = available - {0}, version = version + 1 WHERE id = {1} and available >= #{2}", senderAmount, senderBalanceObj.getId(), senderAmount);
        var recipientPendingAddSqlStr = MessageFormat.format("update balance SET pending = pending + {0}, version = version + 1 WHERE id = {1}", recipientAmount, recipientBalanceObj.getId());
        String[] sqlList = {senderPendingAddSqlStr, senderAvailableSubSqlStr, recipientPendingAddSqlStr};
        balanceTransaction.setSqlExecuteList(sqlList);
        this.balanceTransactionService.saveBalanceToBalanceTx(balanceTransaction, senderBalance, recipientBalance);
        return transaction;
    }

    /**
     * 内部转账更新成失败
     */
    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public Transaction internalBalanceTransferPendingToFailV2(String transactionId) {
        var wrapper = new LambdaQueryWrapper<Transaction>();
        wrapper.eq(Transaction::getId, transactionId);
        wrapper.eq(Transaction::getBalanceOperationType, BalanceOperationTypeEnum.SystemInternalTransfer);
        wrapper.eq(Transaction::getStatus, TransactionStatusEnum.Pending);
        var transaction = transactionMapper.selectOne(wrapper);
        if (transaction == null) {
            throw new CustomException("id is Error", HttpStatus.BAD_REQUEST);
        }

        transaction.setStatus(TransactionStatusEnum.Fail);
        transactionMapper.updateById(transaction);

        var senderBalanceObj = balanceService.getById(transaction.getSenderBalanceId());
        if (senderBalanceObj == null || !senderBalanceObj.getCurrency().equals(transaction.getSenderCurrency())) {
            throw new CustomException("id is Error", HttpStatus.BAD_REQUEST);
        }

        var recipientBalanceObj = balanceService.getById(transaction.getRecipientBalanceId());
        if (recipientBalanceObj == null || !recipientBalanceObj.getCurrency().equals(transaction.getRecipientCurrency())) {
            throw new CustomException("id is Error", HttpStatus.BAD_REQUEST);
        }

        var senderCost = transaction.getSenderCost();
        var senderFee = transaction.getSenderFee();
        var senderAmount = senderCost.add(senderFee);
        BigDecimal recipientFee = transaction.getRecipientFee();
        recipientFee = recipientFee == null ? BigDecimal.ZERO : recipientFee;
        var recipientAmount = transaction.getRecipientCost().subtract(recipientFee);

        // senderBalance pending-, Available+
        balanceService.balanceSubPendingAmount(senderBalanceObj.getId(), senderAmount);
        balanceService.balanceAddAvailableAmount(senderBalanceObj.getId(), senderAmount);

        // recipient pending-,
        balanceService.balanceSubPendingAmount(recipientBalanceObj.getId(), recipientAmount);

        // 保存其他信息
        var senderBalance = balanceService.getById(senderBalanceObj.getId());
        var recipientBalance = balanceService.getById(recipientBalanceObj.getId());
        var balanceTransaction = balanceTransactionService.createBalanceTransaction(transaction);
        var senderPendingAddSqlStr = MessageFormat.format("update balance SET pending = pending - {0}, version = version + 1 WHERE id = {1} and pending >= {2}", senderAmount, senderBalanceObj.getId(), senderAmount);
        var senderAvailableSubSqlStr = MessageFormat.format("update balance SET pending = pending - {0}, version = version + 1 WHERE id = {1} and pending >= {2}", recipientAmount, recipientBalanceObj.getId(), recipientAmount);
        var recipientAvailableAddSqlStr = MessageFormat.format("update balance SET available = available + {0}, version = version + 1 WHERE id = {1}", recipientAmount, recipientBalanceObj.getId());
        String[] sqlList = {senderPendingAddSqlStr, senderAvailableSubSqlStr, recipientAvailableAddSqlStr};
        balanceTransaction.setSqlExecuteList(sqlList);
        this.balanceTransactionService.saveBalanceToBalanceTx(balanceTransaction, senderBalance, recipientBalance);
        return transaction;
    }

    /**
     * 获取一个数字的 id 如：2202251427701399
     *
     * @param date
     * @return
     */
    @Override
    public String getTransactionId(Date date) {
        var dateString = "";
        var key = "transaction:transactionId";
        var transactionNo = redisTemplate.opsForValue().increment(key);
        if (transactionNo == 999000) {
            this.redisTemplate.delete(key);
        }
        if (date == null) {
            SimpleDateFormat formatter = new SimpleDateFormat("YYMMDDHHmm");
            formatter.setTimeZone(TimeZone.getTimeZone("Etc/GMT-8"));
            dateString = formatter.format(new Date());
        }
        var str = ("000000" + transactionNo);
        var tempStr = str.substring(str.length() - 6);
        return dateString + tempStr;
    }

    /**
     * 创建 Transaction
     *
     * @param params
     * @return
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public Transaction createAddTransaction(BalanceChangeDTO params) {
        Transaction transaction = new Transaction();
        transaction.setRemarks(params.getRemarks());
        transaction.setSourceId(params.getSourceId());
        transaction.setSourceType(params.getSourceType().toString());
        transaction.setType(params.getType());
        transaction.setStatus(TransactionStatusEnum.Pending);
        transaction.setAccountId(params.getAccountId());
        transaction.setSender("");
        transaction.setSenderBalanceId(null);
        transaction.setSenderType("");
        transaction.setSenderCurrency(params.getCurrency());
        transaction.setSenderFee(BigDecimal.ZERO);
        transaction.setSenderCost(BigDecimal.ZERO);
        transaction.setRecipient("");
        transaction.setRecipientBalanceId(params.getBalanceId());
        transaction.setRecipientType(params.getSourceType().toString());
        transaction.setRecipientCurrency(params.getCurrency());
        transaction.setRecipientFee(params.getFee());
        transaction.setRecipientCost(params.getCost());
        transaction.setBalanceOperationType(BalanceOperationTypeEnum.SingleBalanceAdd.toString());
        transaction.setTransactionDisplayId(getTransactionId(null));
        transaction.setTransactionTime(params.getTransactionTime() == null ? new Date() : params.getTransactionTime());
        this.save(transaction);
        return transaction;
    }

    /**
     * 创建 Transaction
     *
     * @param params
     * @return
     */
    @Transactional
    public Transaction createSubTransaction(BalanceChangeDTO params) {
        var transaction = new Transaction();
        transaction.setRemarks(params.getRemarks());
        transaction.setSourceId(params.getSourceId());
        transaction.setSourceType(params.getSourceType().toString());
        transaction.setType(params.getType());
        transaction.setStatus(TransactionStatusEnum.Pending);
        transaction.setAccountId(params.getAccountId());
        transaction.setSender("");
        transaction.setSenderBalanceId(params.getBalanceId());
        transaction.setSenderType(params.getSourceType().toString());
        transaction.setSenderCurrency(params.getCurrency());
        transaction.setSenderFee(params.getFee());
        transaction.setSenderCost(params.getCost());
        transaction.setRecipient("");
        transaction.setRecipientType("");
        transaction.setRecipientCurrency(params.getCurrency());
        transaction.setRecipientFee(BigDecimal.ZERO);
        transaction.setRecipientCost(BigDecimal.ZERO);
        transaction.setBalanceOperationType(BalanceOperationTypeEnum.SingleBalanceSub.toString());
        transaction.setTransactionDisplayId(getTransactionId(null));
        transaction.setTransactionTime(params.getTransactionTime() == null ? new Date() : params.getTransactionTime());
        this.save(transaction);
        return transaction;
    }

    /**
     * 创建 内部转账 交易
     *
     * @param subBalanceParams
     * @param addBalanceParams
     * @return
     */
    private Transaction createInternalBalanceTransferTx(BalanceChangeDTO subBalanceParams, BalanceChangeDTO addBalanceParams) {
        var transaction = new Transaction();
        transaction.setSourceId(subBalanceParams.getSourceId());
        transaction.setSourceType(subBalanceParams.getSourceType().toString());
        transaction.setType(subBalanceParams.getType());
        transaction.setStatus(TransactionStatusEnum.Pending);
        transaction.setAccountId(subBalanceParams.getAccountId());
        transaction.setSender("");
        transaction.setSenderBalanceId(subBalanceParams.getBalanceId());
        transaction.setSenderType(subBalanceParams.getSourceType().toString());
        transaction.setSenderCurrency(subBalanceParams.getCurrency());
        transaction.setSenderFee(subBalanceParams.getFee());
        transaction.setSenderCost(subBalanceParams.getCost());
        transaction.setRecipient("");
        transaction.setRecipientBalanceId(addBalanceParams.getBalanceId());
        transaction.setRecipientType(addBalanceParams.getSourceType().toString());
        transaction.setRecipientCurrency(addBalanceParams.getCurrency());
        transaction.setRecipientFee(addBalanceParams.getFee());
        transaction.setRecipientCost(addBalanceParams.getCost());
        transaction.setBalanceOperationType(BalanceOperationTypeEnum.SystemInternalTransfer.toString());
        transaction.setTransactionDisplayId(getTransactionId(null));
        transaction.setTransactionTime(subBalanceParams.getTransactionTime() == null ? new Date() : subBalanceParams.getTransactionTime());

        // 保存 transaction
        this.save(transaction);
        return transaction;
    }
}
