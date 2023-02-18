package com.qbit.assets.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.common.enums.BalanceColumnTypeEnum;
import com.qbit.assets.common.enums.SpecialUUID;
import com.qbit.assets.common.error.CustomException;
import com.qbit.assets.common.utils.RedisLockUtil;
import com.qbit.assets.domain.entity.Balance;
import com.qbit.assets.domain.vo.AccountBalanceVO;
import com.qbit.assets.mapper.BalanceMapper;
import com.qbit.assets.service.BalanceService;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.BalanceVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.AssetsBalanceVO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author martinjiang
 */
@Service
@Slf4j
public class BalanceServiceImpl extends ServiceImpl<BalanceMapper, Balance> implements BalanceService {

    @Resource
    private BalanceMapper balanceMapper;
    @Resource
    private RedisLockUtil redisLockUtil;


    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public boolean balanceAddAvailableSubPending(String balanceId, BigDecimal amount) {
        this.balanceAddAvailableAmount(balanceId, amount);
        this.balanceSubPendingAmount(balanceId, amount);
        return true;
    }

    /**
     * 钱包 available 增加 pending
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public boolean balanceSubAvailableAddPending(String balanceId, BigDecimal amount) {
        this.balanceSubAvailableAmount(balanceId, amount);
        this.balanceAddPendingAmount(balanceId, amount);
        return true;
    }

    /**
     * 钱包 增加 pending
     *
     * @param balanceId balance id
     * @param amount    amount
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public boolean balanceAddPendingAmount(String balanceId, BigDecimal amount) {
        var cost = amount.abs();
        RLock rLock = redisLockUtil.reentrantLock("qbitpay:3:redisLock:balance:" + balanceId);
        try {
            Boolean aBoolean = redisLockUtil.setLock(rLock, 0, 3600);
            log.debug("balance:" + balanceId + "TID" + Thread.currentThread().getName() + "获取锁");
            Balance balanceObj = getBalanceByOrderAndCheck(balanceId);
            int rows = balanceMapper.addBalancePending(balanceObj.getId(), cost);
            if (rows != 1) {
                throw new Error("balance pending add amount is error");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            redisLockUtil.unLock(rLock);
            log.debug("balance:" + balanceId + "TID" + Thread.currentThread().getName() + "释放锁");
        }
        return true;
    }


    @Override
    public Balance getValidated(String id) {
        return getValidated(id);
        //return getValidated(id, null);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Balance checkBalanceAmountWithError(String balanceId, BigDecimal amount, BalanceColumnTypeEnum column) {
        var balance = this.checkBalance(balanceId);
        var verified = this.checkBalanceAmount(balanceId, amount, column);
        if (!verified) {
            throw new CustomException("insufficient amount");
        }
        return balance;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public boolean checkBalanceAmount(String balanceId, BigDecimal amount, BalanceColumnTypeEnum column) {
        var resFlag = false;
        var balance = balanceMapper.selectById(balanceId);
        if (column == BalanceColumnTypeEnum.Available) {
            var res = balance.getAvailable().subtract(amount);
            if (res.compareTo(BigDecimal.ZERO) >= 0) {
                // Available >= amount
                resFlag = true;
            }
        } else if (column == BalanceColumnTypeEnum.Pending) {
            var res = balance.getPending().subtract(amount);
            if (res.compareTo(BigDecimal.ZERO) >= 0) {
                // Pending >= amount
                resFlag = true;
            }
        } else if (column == BalanceColumnTypeEnum.Frozen) {
            var res = balance.getPending().subtract(amount);
            if (res.compareTo(BigDecimal.ZERO) >= 0) {
                // Frozen >= amount
                resFlag = true;
            }
        } else {
            throw new CustomException("column is error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resFlag;
    }


    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Balance checkBalance(String balanceId) {
        var balance = balanceMapper.selectById(balanceId);
        //var balance = balanceMapper.findBalanceByIdForUpdate(balanceId);
        if (balance == null) {
            throw new CustomException("balanceId is null");
        }
        return balance;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Balance checkBalance(String balanceId, String accountId) {
        Balance balance = checkBalance(balanceId);
        if (!balance.getAccountId().equals(accountId)) {
            throw new CustomException("balanceId is null");
        }
        return balance;
    }


    /**
     * 换算成法币金额
     *
     * @param amount    amount
     * @param rate      汇率
     * @param precision 精度
     * @return amount
     */
    private BigDecimal getLegalAmount(BigDecimal amount, BigDecimal rate, int precision) {
        return amount.multiply(rate).setScale(precision, RoundingMode.FLOOR);
    }


    @Override
    public Balance findBalanceByIdForUpdate(String id) {
        return this.balanceMapper.findBalanceByIdForUpdate(id);
    }

    @Override
    public List<AssetsBalanceVO> getOkxBalances(String[] ccys, String walletType, String accountId) {
        LambdaQueryWrapper<Balance> balanceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ccys != null && ccys.length > 0) {
            balanceLambdaQueryWrapper.in(Balance::getCurrency, ccys);
        }
        List<AssetsBalanceVO> assetsBalances = new ArrayList<>();
        balanceLambdaQueryWrapper.eq(Balance::getAccountId, SpecialUUID.NullUUID.value());
        balanceLambdaQueryWrapper.eq(Balance::getWalletType, "OkxWallet");
        List<Balance> balances = balanceMapper.selectList(balanceLambdaQueryWrapper);
        if (CollectionUtil.isNotEmpty(balances)) {
            assetsBalances = balances.stream().map(e -> {
                AssetsBalanceVO assetsBalanceVO = new AssetsBalanceVO();
                assetsBalanceVO.setCcy(e.getCurrency().getValue());
                assetsBalanceVO.setBal(e.getAvailable().toString());
                assetsBalanceVO.setAvailBal(e.getAvailable().toString());
                assetsBalanceVO.setFrozenBal(e.getFrozen().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                return assetsBalanceVO;
            }).collect(Collectors.toList());
        }
        return assetsBalances;
    }

    @Override
    public AccountBalanceVO getCircleBalances() {
        LambdaQueryWrapper<Balance> balanceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        AccountBalanceVO assetsBalances = new AccountBalanceVO();
        balanceLambdaQueryWrapper.eq(Balance::getAccountId, SpecialUUID.NullUUID.value());
        balanceLambdaQueryWrapper.eq(Balance::getWalletType, "CircleWallet");
        List<Balance> balances = balanceMapper.selectList(balanceLambdaQueryWrapper);
        if (CollectionUtil.isNotEmpty(balances)) {
            List<BalanceVO> available = balances.stream().map(e -> {
                BalanceVO balanceVO = new BalanceVO();
                balanceVO.setCurrency(e.getCurrency().getValue());
                balanceVO.setAmount(e.getAvailable().toString());
                return balanceVO;
            }).collect(Collectors.toList());
            assetsBalances.setAvailable(available);
            List<BalanceVO> unsettled = new ArrayList<>();
            assetsBalances.setUnsettled(unsettled);
        }
        return assetsBalances;
    }

    /**
     * 钱包 减少 pending
     *
     * @param balanceId balance id
     * @param amount    amount
     * @return boolean
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public boolean balanceSubPendingAmount(String balanceId, BigDecimal amount) {
        RLock rLock = redisLockUtil.reentrantLock("qbitpay:3:redisLock:balance:" + balanceId);
        try {
            Boolean aBoolean = redisLockUtil.setLock(rLock, 0, 3600);
            log.debug("balance:" + balanceId + "TID" + Thread.currentThread().getName() + "获取锁");
            var cost = amount.abs();
            Balance balanceObj = getBalanceByOrderAndCheck(balanceId);
            int rows = balanceMapper.subBalancePending(balanceObj.getId(), cost);
            if (rows != 1) {
                throw new Error("balance pending sub amount is error");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            redisLockUtil.unLock(rLock);
            log.debug("balance:" + balanceId + "TID" + Thread.currentThread().getName() + "释放锁");
        }
        return true;
    }

    /**
     * 钱包 增加 可用
     *
     * @param balanceId balance id
     * @param amount    amount
     * @return boolean
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public boolean balanceAddAvailableAmount(String balanceId, BigDecimal amount) {
        RLock rLock = redisLockUtil.reentrantLock("qbitpay:3:redisLock:balance:" + balanceId);
        try {
            Boolean aBoolean = redisLockUtil.setLock(rLock, 0, 3600);
            log.debug("balance:" + balanceId + "TID" + Thread.currentThread().getName() + "获取锁");
            var cost = amount.abs();
            var balanceObj = this.getBalanceByOrderAndCheck(balanceId);
            int rows = balanceMapper.addBalanceAvailable(balanceObj.getId(), cost);
            if (rows != 1) {
                throw new Error("balance available add amount is error");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            redisLockUtil.unLock(rLock);
            log.debug("balance:" + balanceId + "TID" + Thread.currentThread().getName() + "释放锁");
        }
        return true;
    }

    /**
     * 钱包 减少 可用
     *
     * @param balanceId balance id
     * @param amount    amount
     * @return boolean
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public boolean balanceSubAvailableAmount(String balanceId, BigDecimal amount) {
        RLock rLock = redisLockUtil.reentrantLock("qbitpay:3:redisLock:balance:" + balanceId);
        try {
            Boolean aBoolean = redisLockUtil.setLock(rLock, 0, 3600);
            log.debug("balance:" + balanceId + "TID" + Thread.currentThread().getName() + "获取锁");
            var cost = amount.abs();
            var balanceObj = this.getBalanceByOrderAndCheck(balanceId);
            int rows = balanceMapper.subBalanceAvailable(balanceObj.getId(), cost);
            if (rows != 1) {
                throw new Error("balance available sub amount is error");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            redisLockUtil.unLock(rLock);
            log.debug("balance:" + balanceId + "TID" + Thread.currentThread().getName() + "释放锁");
        }
        return true;
    }

    /**
     * 钱包 增加 冻结
     *
     * @param balanceId balance id
     * @param amount    amount
     * @return boolean
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public boolean balanceAddFrozenAmount(String balanceId, BigDecimal amount) {
        RLock rLock = redisLockUtil.reentrantLock("qbitpay:3:redisLock:balance:" + balanceId);
        try {
            Boolean aBoolean = redisLockUtil.setLock(rLock, 0, 3600);
            log.debug("balance:" + balanceId + "TID" + Thread.currentThread().getName() + "获取锁");
            var cost = amount.abs();
            var balanceObj = this.getBalanceByOrderAndCheck(balanceId);
            int rows = balanceMapper.addBalanceFrozen(balanceObj.getId(), cost);
            if (rows != 1) {
                throw new Error("balance frozen add amount is error");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            redisLockUtil.unLock(rLock);
            log.debug("balance:" + balanceId + "TID" + Thread.currentThread().getName() + "释放锁");
        }
        return true;
    }

    /**
     * 钱包 减少 冻结
     *
     * @param balanceId balance id
     * @param amount    amount
     * @return boolean
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public boolean balanceSubFrozenAmount(String balanceId, BigDecimal amount) {
        RLock rLock = redisLockUtil.reentrantLock("qbitpay:3:redisLock:balance:" + balanceId);
        try {
            Boolean aBoolean = redisLockUtil.setLock(rLock, 0, 3600);
            log.debug("balance:" + balanceId + "TID" + Thread.currentThread().getName() + "获取锁");
            var cost = amount.abs();
            var balanceObj = this.getBalanceByOrderAndCheck(balanceId);
            int rows = balanceMapper.subBalanceFrozen(balanceObj.getId(), cost);
            if (rows != 1) {
                throw new Error("balance frozen sub amount is error");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            redisLockUtil.unLock(rLock);
            log.debug("balance:" + balanceId + "TID" + Thread.currentThread().getName() + "释放锁");
        }
        return true;
    }


    /**
     * 获取钱包对象
     *
     * @param balanceId String
     * @return Balance
     */
    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Throwable.class)
    public Balance getBalanceByOrderAndCheck(String balanceId) {
        var balanceObj = this.balanceMapper.selectById(balanceId);
        //var balanceObj = this.balanceMapper.findBalanceByIdForUpdate(balanceId);
        if (balanceObj == null) {
            throw new Error("balance id is error");
        }
        if (balanceObj.getDeleteTime() != null) {
            throw new Error("balance is deleted");
        }
        return balanceObj;
    }
}
