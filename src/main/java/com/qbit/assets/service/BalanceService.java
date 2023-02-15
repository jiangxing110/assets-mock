package com.qbit.assets.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qbit.assets.domain.entity.Balance;
import com.qbit.assets.domain.vo.AccountBalanceVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.AssetsBalanceVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author martin
 * @since 2023-02-04
 */
@Service
public interface BalanceService extends IService<Balance> {

    boolean balanceSubPendingAmount(String balanceId, BigDecimal amount);

    boolean balanceSubAvailableAmount(String balanceId, BigDecimal amount);

    boolean balanceAddAvailableAmount(String balanceId, BigDecimal amount);

    boolean balanceAddPendingAmount(String balanceId, BigDecimal amount);

    boolean balanceAddFrozenAmount(String balanceId, BigDecimal amount);

    boolean balanceSubFrozenAmount(String balanceId, BigDecimal amount);


    /**
     * 获取经过验证的balance实体
     *
     * @param id balance id
     * @return {@link Balance}
     */
    Balance getValidated(String id);


    /**
     * 检查钱包
     *
     * @param balanceId 钱包id
     * @return {@link Balance}
     */
    Balance checkBalance(String balanceId);

    /**
     * 检查钱包
     *
     * @param balanceId 钱包id
     * @param accountId 账户id
     * @return {@link Balance}
     */
    Balance checkBalance(String balanceId, String accountId);


    /**
     * 排他锁
     *
     * @param id
     * @return
     */
    Balance findBalanceByIdForUpdate(String id);

    /**
     * 根据钱包类型币种获取钱包列表
     */
    List<AssetsBalanceVO> getOkxBalances(String[] ccy, String walletType, String accountId);

    AccountBalanceVO getCircleBalances();
}
