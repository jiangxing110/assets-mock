package com.qbit.assets.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.common.error.CustomException;
import com.qbit.assets.common.utils.RedisLockUtil;
import com.qbit.assets.domain.entity.Balance;
import com.qbit.assets.mapper.BalanceMapper;
import com.qbit.assets.service.BalanceService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;

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
