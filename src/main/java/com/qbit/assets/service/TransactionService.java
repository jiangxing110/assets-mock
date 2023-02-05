package com.qbit.assets.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.qbit.assets.common.enums.TransactionStatusEnum;
import com.qbit.assets.domain.dto.BalanceChangeDTO;
import com.qbit.assets.domain.entity.Transaction;

import java.util.Date;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author martin
 * @since 2023-02-05
 */
public interface TransactionService extends IService<Transaction> {
    /**
     * 获取一个数字的 id
     *
     * @param date
     * @return
     */
    String getTransactionId(Date date);

    /**
     * 所有订单更新的入口
     *
     * @param transactionId
     * @param status
     * @return
     */
    Transaction updateBalanceByBalanceTransactionIdV2(String transactionId, TransactionStatusEnum status);

    /**
     * 单钱包加钱后pending
     *
     * @param params
     * @return
     */
    Transaction singleBalanceAddAmountToPendingV2(BalanceChangeDTO params);

    /**
     * 单钱包减钱后pending
     *
     * @param params
     * @return
     */
    Transaction singleBalanceSubAmountToPendingV2(BalanceChangeDTO params);

    /**
     * 内部转账pending
     *
     * @param subBalanceParams
     * @param addBalanceParams
     * @return
     */
    Transaction internalBalanceTransferPendingV2(BalanceChangeDTO subBalanceParams, BalanceChangeDTO addBalanceParams);

    /**
     * 冻结钱包的可用
     *
     * @param params
     * @return
     */
    Transaction frozenAvailableAmount(BalanceChangeDTO params);

    /**
     * 解冻钱包的可用
     *
     * @param params
     * @return
     */
    Transaction unFrozenAvailableAmount(BalanceChangeDTO params);
}
