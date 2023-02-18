package com.qbit.assets.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.common.enums.*;
import com.qbit.assets.common.error.CustomException;
import com.qbit.assets.domain.dto.AssetTransferDto;
import com.qbit.assets.domain.dto.BalanceChangeDTO;
import com.qbit.assets.domain.entity.Balance;
import com.qbit.assets.domain.entity.CryptoAssetsTransfer;
import com.qbit.assets.domain.entity.Transaction;
import com.qbit.assets.mapper.CryptoAssetsTransferMapper;
import com.qbit.assets.service.BalanceService;
import com.qbit.assets.service.CryptoAssetsTransferService;
import com.qbit.assets.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

import static com.qbit.assets.common.enums.CryptoAssetsTransferStatus.Pending;
import static com.qbit.assets.common.enums.CryptoAssetsTransferStatus.Processing;

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
public class CryptoAssetsTransferServiceImpl extends ServiceImpl<CryptoAssetsTransferMapper, CryptoAssetsTransfer> implements CryptoAssetsTransferService {

    @Resource
    private BalanceService balanceService;

    @Resource
    private TransactionService transactionService;

    /**
     * 转入转出审批
     */
    @Override
    public CryptoAssetsTransfer review(String transferId, CryptoAssetsTransferStatus status) {
        CryptoAssetsTransfer oldTransfer = this.getById(transferId);
        CryptoAssetsTransferStatus oldStatus = oldTransfer.getStatus();
        if (Pending != oldStatus) {
            throw new CustomException("当前状态不可审批");
        }
        TransactionStatusEnum displayStatus = TransactionStatusEnum.formStatus(status);
        oldTransfer.setId(oldTransfer.getId());
        oldTransfer.setStatus(status);
        oldTransfer.setDisplayStatus(displayStatus);
        oldTransfer.setVersion(oldTransfer.getVersion());
        transactionService.updateBalanceByBalanceTransactionIdV2(oldTransfer.getTransactionId(), displayStatus);
        this.updateById(oldTransfer);
        return oldTransfer;
    }

    /**
     * 充值
     */
    @Override
    public CryptoAssetsTransfer deposit(AssetTransferDto body) {
        var wrapper = new LambdaQueryWrapper<CryptoAssetsTransfer>();
        wrapper.eq(CryptoAssetsTransfer::getTradeId, body.getTradeId());
        var cryptoAssetsTransferDb = this.getOne(wrapper);
        if (cryptoAssetsTransferDb != null) {
            throw new CustomException("订单已存在");
        }
        Balance balance = balanceService.getById(body.getInBalanceId());
        CryptoAssetsTransfer transfer = new CryptoAssetsTransfer();
        // 交易类型
        TransactionTypeEnum type = body.getType();
        CryptoAssetsTransferStatus status = Pending;
        if (type == null) {
            type = TransactionTypeEnum.OtherChannelToVirtualUSDTransferIn;
        }
        mappingType(type, transfer);
        transfer.setAction(CryptoAssetsTransferAction.OUT);
        BigDecimal settlementAmount = body.getAmount();
        BigDecimal fee = body.getFee();
        transfer = transfer(balance.getId(), settlementAmount, fee, type, BalanceOperationEnum.Add, body.getTradeId(), transfer.getId());
        transfer.setStatus(status);
        this.updateById(transfer);
        return transfer;
    }

    /**
     * 提现
     */
    @Override
    public CryptoAssetsTransfer withdraw(AssetTransferDto body) {
        var wrapper = new LambdaQueryWrapper<CryptoAssetsTransfer>();
        wrapper.eq(CryptoAssetsTransfer::getTradeId, body.getTradeId());
        var cryptoAssetsTransferDb = this.getOne(wrapper);
        if (cryptoAssetsTransferDb != null) {
            throw new CustomException("订单已存在");
        }
        Balance balance = balanceService.getById(body.getOutBalanceId());
        CryptoAssetsTransfer transfer = new CryptoAssetsTransfer();
        TransactionTypeEnum type = body.getType();
        CryptoAssetsTransferStatus status = Pending;
        if (type == null) {
            type = TransactionTypeEnum.VirtualUSDTransferToOtherChannel;
        }
        mappingType(type, transfer);
        transfer.setAction(CryptoAssetsTransferAction.OUT);
        BigDecimal originAmount = body.getAmount();
        BigDecimal settlementAmount = originAmount.subtract(body.getFee());
        BigDecimal fee = body.getFee();
        transfer = transfer(balance.getId(), settlementAmount, fee, type, BalanceOperationEnum.Sub, body.getTradeId(), transfer.getId());
        transfer.setStatus(status);
        this.updateById(transfer);
        return transfer;
    }

    @Transactional(rollbackFor = Throwable.class)
    public CryptoAssetsTransfer transfer(String balanceId, BigDecimal amount, BigDecimal fee, TransactionTypeEnum type, BalanceOperationEnum operation, String tradeId, String id) {
        BigDecimal totalAmount = amount.add(fee);
        TransactionTypeEnum[] types = {TransactionTypeEnum.OtherChannelToVirtualUSDTransferIn};
        Balance balance;
        if (operation == BalanceOperationEnum.Add) {
            balance = balanceService.checkBalance(balanceId);
        } else {
            // 出金验证账户金额
            balance = balanceService.checkBalanceAmountWithError(balanceId, totalAmount, BalanceColumnTypeEnum.Available);
        }
        WalletTypeEnum walletType = balance.getWalletType();

        if (walletType != WalletTypeEnum.CircleWallet && walletType != WalletTypeEnum.OkxWallet && walletType != WalletTypeEnum.VirtualUSD) {
            throw new CustomException("不支持的钱包类型");
        }

        amount = amount.subtract(fee);

        BalanceChangeDTO params = buildTransferDTO(balance, amount, fee, operation);
        Transaction transaction;

        if (operation == BalanceOperationEnum.Add) {
            transaction = this.transactionService.singleBalanceAddAmountToPendingV2(params);
        } else {
            transaction = this.transactionService.singleBalanceSubAmountToPendingV2(params);
        }
        CryptoAssetsTransfer transfer = createEntity(transaction);
        transfer.setTradeId(tradeId);
        mappingType(type, transfer);

        transfer.setOriginAmount(amount.add(fee));
        transfer.setSettlementAmount(amount);
        if (id != null) {
            transfer.setId(id);
        }
        transfer.setFee(fee);
        this.save(transfer);
        return transfer;
    }

    /**
     * 构建内部交易params
     *
     * @param balance   balance
     * @param amount    交易金额
     * @param fee       交易手续费
     * @param operation sub/add
     * @return params
     */
    @Override
    public BalanceChangeDTO buildTransferDTO(Balance balance, BigDecimal amount, BigDecimal fee, BalanceOperationEnum operation) {
        BalanceChangeDTO params = new BalanceChangeDTO();
        params.setBalanceId(balance.getId());
        params.setCurrency(balance.getCurrency());
        params.setAccountId(balance.getAccountId());
        if (operation == BalanceOperationEnum.Sub) {
            params.setFee(fee);
        }
        params.setCost(amount);
        params.setOperation(operation);
        params.setType(operation == BalanceOperationEnum.Sub ? TransactionTypeEnum.CryptoAssetsTransferOut : TransactionTypeEnum.CryptoAssetsTransferIn);
        params.setSourceType(TransactionSourceTypeEnum.QbitCrypto);
        return params;
    }

    private void mappingType(TransactionTypeEnum type, CryptoAssetsTransfer transfer) {
        CounterpartyType senderType = CounterpartyType.WALLET;
        CounterpartyType recipientType = CounterpartyType.WALLET;
        switch (type) {
            case CryptoAssetsTransferIn:
                senderType = CounterpartyType.CHAIN;
                break;
            case CryptoAssetsTransferOut:
                recipientType = CounterpartyType.CHAIN;
                break;
            case CircleWalletToWire:
                recipientType = CounterpartyType.WIRE;
                break;
            case OtherChannelToVirtualUSDTransferIn:
                senderType = CounterpartyType.OUTSIDE_BANK;
                break;
            case VirtualUSDTransferToOtherChannel:
                recipientType = CounterpartyType.OUTSIDE_BANK;
                break;
            default:
                break;
        }
        transfer.setSenderType(senderType);
        transfer.setRecipientType(recipientType);
    }

    @Override
    public CryptoAssetsTransfer createEntity(Transaction transaction) {
        CryptoAssetsTransferAction action = CryptoAssetsTransferAction.IN;
        CryptoAssetsTransfer transfer = new CryptoAssetsTransfer();
        if (transaction.getType().equals(TransactionTypeEnum.CryptoAssetsTransferIn)) {
            transfer.setCurrency(transaction.getRecipientCurrency());
            transfer.setQuoteCurrency(transaction.getSenderCurrency());
            transfer.setBalanceId(transaction.getRecipientBalanceId());
        } else if (transaction.getType().equals(TransactionTypeEnum.CryptoAssetsTransferOut)) {
            action = CryptoAssetsTransferAction.OUT;
            transfer.setCurrency(transaction.getSenderCurrency());
            transfer.setQuoteCurrency(transaction.getRecipientCurrency());
            transfer.setBalanceId(transaction.getSenderBalanceId());
        } else {
            throw new CustomException("不支持的交易类型");
        }
        transfer.setAccountId(transaction.getAccountId());
        transfer.setRate(BigDecimal.ONE);
        transfer.setAction(action);
        transfer.setChain(ChainType.NA);
        transfer.setOriginAmount(BigDecimal.ZERO);
        transfer.setSettlementAmount(BigDecimal.ZERO);
        transfer.setFee(BigDecimal.ZERO);
        transfer.setStatus(Processing);
        transfer.setDisplayStatus(TransactionStatusEnum.Pending);
        transfer.setTransactionId(transaction.getId());
        transfer.setTransactionTime(transaction.getTransactionTime());
        return transfer;
    }


}
