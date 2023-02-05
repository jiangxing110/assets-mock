package com.qbit.assets.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.qbit.assets.domain.entity.Balance;
import com.qbit.assets.domain.entity.BalanceTransaction;
import com.qbit.assets.domain.entity.Transaction;

/**
 * @author martinjiang
 */
public interface BalanceTransactionService extends IService<BalanceTransaction> {

    BalanceTransaction createBalanceTransaction(Transaction transactionObj);

    /**
     * 保存时点余额
     *
     * @param btx
     * @param senderBalance
     * @param recipientBalance
     * @return
     */
    BalanceTransaction saveBalanceToBalanceTx(BalanceTransaction btx, Balance senderBalance, Balance recipientBalance);
}
