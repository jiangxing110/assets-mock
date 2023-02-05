package com.qbit.assets.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.common.error.CustomException;
import com.qbit.assets.domain.entity.Balance;
import com.qbit.assets.domain.entity.BalanceTransaction;
import com.qbit.assets.domain.entity.Transaction;
import com.qbit.assets.mapper.BalanceTransactionMapper;
import com.qbit.assets.service.BalanceTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author martin
 * @since 2023-02-05
 */
@Service
@Slf4j
public class BalanceTransactionServiceImpl extends ServiceImpl<BalanceTransactionMapper, BalanceTransaction> implements BalanceTransactionService {
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY)
    public BalanceTransaction createBalanceTransaction(Transaction transactionObj) {
        var balanceTransaction = new BalanceTransaction();
        balanceTransaction.setSourceId(transactionObj.getId());
        balanceTransaction.setSourceType(transactionObj.getSourceType());
        balanceTransaction.setType(transactionObj.getStatus());
        balanceTransaction.setAccountId(transactionObj.getAccountId());
        balanceTransaction.setSender(transactionObj.getSender());
        String senderBalanceId = transactionObj.getSenderBalanceId();
        if (StringUtils.isNoneEmpty(senderBalanceId)) {
            balanceTransaction.setSenderBalanceId(senderBalanceId);
        }
        balanceTransaction.setSenderType(transactionObj.getSenderType());
        balanceTransaction.setSenderCurrency(transactionObj.getSenderCurrency());
        balanceTransaction.setSenderFee(transactionObj.getSenderFee());
        balanceTransaction.setSenderCost(transactionObj.getSenderCost());
        balanceTransaction.setRecipient(transactionObj.getRecipient());
        String recipientBalanceId = transactionObj.getRecipientBalanceId();

        if (StringUtils.isNoneEmpty(recipientBalanceId)) {
            balanceTransaction.setRecipientBalanceId(recipientBalanceId);
        }
        balanceTransaction.setRecipientType(transactionObj.getRecipientType());
        balanceTransaction.setRecipientCurrency(transactionObj.getRecipientCurrency());
        balanceTransaction.setRecipientFee(transactionObj.getRecipientFee());
        balanceTransaction.setRecipientCost(transactionObj.getRecipientCost());
        balanceTransaction.setRemarks(transactionObj.getRemarks());
        this.save(balanceTransaction);
        return balanceTransaction;
    }

    /**
     * 保存时点余额
     *
     * @param btx              BalanceTransaction
     * @param senderBalance    sender
     * @param recipientBalance recipient
     * @return BalanceTransaction
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY)
    public BalanceTransaction saveBalanceToBalanceTx(BalanceTransaction btx, Balance senderBalance, Balance recipientBalance) {
        if (senderBalance != null) {
            btx.setSenderCurrentAvailableAmount(senderBalance.getAvailable());
            btx.setSenderCurrentPendingAmount(senderBalance.getPending());
            btx.setSenderCurrentFrozenAmount(senderBalance.getFrozen());
        }
        if (recipientBalance != null) {
            btx.setRecipientCurrentAvailableAmount(recipientBalance.getAvailable());
            btx.setRecipientCurrentPendingAmount(recipientBalance.getPending());
            btx.setRecipientCurrentFrozenAmount(recipientBalance.getFrozen());
        }

        boolean b = this.updateById(btx);
        if (!b) {
            throw new CustomException("订单状态更新失败");
        }
        return btx;
    }
}
