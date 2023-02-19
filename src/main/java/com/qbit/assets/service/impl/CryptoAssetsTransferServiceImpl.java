package com.qbit.assets.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JavaType;
import com.qbit.assets.common.enums.*;
import com.qbit.assets.common.error.CustomException;
import com.qbit.assets.common.utils.JsonUtil;
import com.qbit.assets.domain.dto.AssetTransferDto;
import com.qbit.assets.domain.dto.BalanceChangeDTO;
import com.qbit.assets.domain.dto.CryptoAssetsTransferDTO;
import com.qbit.assets.domain.entity.Balance;
import com.qbit.assets.domain.entity.CryptoAssetsTransaction;
import com.qbit.assets.domain.entity.CryptoAssetsTransfer;
import com.qbit.assets.domain.entity.Transaction;
import com.qbit.assets.mapper.CryptoAssetsTransferMapper;
import com.qbit.assets.service.AddressesService;
import com.qbit.assets.service.BalanceService;
import com.qbit.assets.service.CryptoAssetsTransferService;
import com.qbit.assets.service.TransactionService;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.AmountBO;
import com.qbit.assets.thirdparty.internal.circle.enums.CircleTransactionStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

import static com.qbit.assets.common.enums.CryptoAssetsTransferStatus.*;

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
    @Resource
    private AddressesService addressesService;

    /**
     * 转入转出审批
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CryptoAssetsTransfer deposit(AssetTransferDto body) {
        if (StringUtils.isNotBlank(body.getTradeId())) {
            var wrapper = new LambdaQueryWrapper<CryptoAssetsTransfer>();
            wrapper.eq(CryptoAssetsTransfer::getTradeId, body.getTradeId());
            var cryptoAssetsTransferDb = this.getOne(wrapper);
            if (cryptoAssetsTransferDb != null) {
                throw new CustomException("订单已存在");
            }
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CryptoAssetsTransfer withdraw(AssetTransferDto body) {
        if (StringUtils.isNotBlank(body.getTradeId())) {
            var wrapper = new LambdaQueryWrapper<CryptoAssetsTransfer>();
            wrapper.eq(CryptoAssetsTransfer::getTradeId, body.getTradeId());
            var cryptoAssetsTransferDb = this.getOne(wrapper);
            if (cryptoAssetsTransferDb != null) {
                throw new CustomException("订单已存在");
            }
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

    /**
     * @param entity
     * @return
     */
    @Override
    public CryptoAssetsTransfer transferIn(CryptoAssetsTransaction entity) {
        //根据address获取balance
        Balance balance = addressesService.getBalanceByAddress(entity.getChain(), entity.getDestinationAddress());
        if (balance == null) {
            log.info("qbit系统没找到对应的钱包: {}", entity.getTradeId());
            return null;
        }
        CryptoAssetsTransfer record = getByTradeId(entity.getTradeId());
        if (record != null && record.getDisplayStatus() != TransactionStatusEnum.Pending) {
            log.info("已经处理完成的订单: {}", entity.getTradeId());
            return null;
        }
        CryptoConversionCurrencyEnum currency = entity.getCurrency();
        //
        currency = entity.getCurrency();
        //currency == CryptoConversionCurrencyEnum.USD ? CryptoConversionCurrencyEnum.USDC : currency;
        String id = UUID.randomUUID().toString();

        BigDecimal amount = entity.getAmount();

        BigDecimal fee = BigDecimal.ZERO;
        //inFee(entity.getAmount(), CryptoConversionCurrencyEnum.USDC, wallet.getAccountId());
        if (fee.compareTo(amount) > 0) {
            fee = amount;
            amount = BigDecimal.ZERO;
        } else {
            amount = amount.subtract(fee);
        }
        String balanceId = balance.getId();
        // 创建变动钱的订单
        BalanceChangeDTO params = new BalanceChangeDTO();
        params.setBalanceId(balance.getId());
        params.setCurrency(currency);
        params.setAccountId(balance.getAccountId());
        params.setCost(amount);
        params.setSourceId(id);
        params.setSourceType(TransactionSourceTypeEnum.QbitCrypto);
        params.setOperation(BalanceOperationEnum.Add);
        params.setType(TransactionTypeEnum.CryptoAssetsTransferIn);
        params.setSourceType(TransactionSourceTypeEnum.QbitCrypto);
        Transaction transaction = this.transactionService.singleBalanceAddAmountToPendingV2(params);

        if (entity.getStatus() == CircleTransactionStatusEnum.COMPLETE) {
            // 完成订单
            transaction = transactionService.updateBalanceByBalanceTransactionIdV2(transaction.getId(), TransactionStatusEnum.Closed);
        } else {
            transaction = transactionService.updateBalanceByBalanceTransactionIdV2(transaction.getId(), TransactionStatusEnum.Fail);
        }
        // 创建USDC入金订单
        CryptoAssetsTransfer transfer = new CryptoAssetsTransfer();
        transfer.setId(id);
        transfer.setBalanceId(balanceId);
        transfer.setTradeId(entity.getTradeId());
        transfer.setAction(CryptoAssetsTransferAction.IN);
        transfer.setChain(entity.getChain());
        transfer.setSettlementAmount(amount);
        transfer.setFee(fee);
        transfer.setStatus(valueOf(transaction.getStatus()));
        transfer.setCurrency(currency);
        transfer.setQuoteCurrency(currency);
        transfer.setAccountId(balance.getAccountId());
        transfer.setOriginAmount(amount.add(fee));
        transfer.setTransactionId(transaction.getId());

        transfer.setSenderType(CounterpartyType.CHAIN);
        transfer.setRecipientType(CounterpartyType.WALLET);
        transfer.setTransactionTime(entity.getCreateTime());
        transfer.setDisplayStatus(transaction.getStatus());
        // 完成订单落库
        this.save(transfer);
        return transfer;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public CryptoAssetsTransfer handleReturn(CryptoAssetsTransaction entity) {
        String balanceId = entity.getDestinationId();
        Balance balance = balanceService.checkBalance(balanceId);
        CryptoAssetsTransfer record = getByTradeId(entity.getTradeId());
        if (record != null && record.getDisplayStatus() != TransactionStatusEnum.Pending) {
            log.info("已经处理完成的订单: {}", entity.getTradeId());
            return null;
        }

        LambdaQueryWrapper<CryptoAssetsTransfer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CryptoAssetsTransfer::getTradeId, entity.getSourceId()).eq(CryptoAssetsTransfer::getAction, CryptoAssetsTransferAction.OUT).last("LIMIT 1");
        CryptoAssetsTransfer transferOut = this.getOne(wrapper);
        if (transferOut == null) {
            log.info("找不到关联支出订单:\n{}", entity);
            return null;
        }
        Transaction transactionOut = transactionService.getById(transferOut.getTransactionId());
        if (transactionOut == null) {
            log.info("找不到关联支出订单的transaction记录:\n{}", transactionOut);
            return null;
        }
        String id = UUID.randomUUID().toString();

        BigDecimal amount = entity.getAmount();

        if (transferOut.getSettlementAmount().compareTo(amount) < 0) {
            log.warn("退款金额超出支出金额:\n{}\n{}", transferOut, entity);
            return null;
        }

        BigDecimal fee = entity.getFee();

        if (fee.compareTo(amount) > 0) {
            fee = amount;
            amount = BigDecimal.ZERO;
        } else {
            amount = amount.subtract(fee);
        }
        // 创建变动钱的订单
        BalanceChangeDTO params = new BalanceChangeDTO();
        params.setBalanceId(balanceId);
        params.setCurrency(balance.getCurrency());
        params.setAccountId(transferOut.getAccountId());
        params.setCost(amount);
        params.setSourceId(id);
        params.setSourceType(TransactionSourceTypeEnum.QbitCrypto);
        params.setOperation(BalanceOperationEnum.Add);
        params.setType(TransactionTypeEnum.CryptoAssetsTransferIn);
        params.setSourceType(TransactionSourceTypeEnum.QbitCrypto);
        Transaction transaction = this.transactionService.singleBalanceAddAmountToPendingV2(params);
        if (entity.getStatus() == CircleTransactionStatusEnum.COMPLETE) {
            // 完成订单
            transaction = transactionService.updateBalanceByBalanceTransactionIdV2(transaction.getId(), TransactionStatusEnum.Closed);
        } else {
            transaction = transactionService.updateBalanceByBalanceTransactionIdV2(transaction.getId(), TransactionStatusEnum.Fail);
        }
        CryptoAssetsTransfer transfer = new CryptoAssetsTransfer();
        transfer.setId(id);
        transfer.setBalanceId(balanceId);
        transfer.setTradeId(entity.getTradeId());
        transfer.setAction(CryptoAssetsTransferAction.REFUND);
        transfer.setChain(entity.getChain());
        transfer.setSettlementAmount(amount);
        transfer.setFee(fee);
        transfer.setStatus(valueOf(transaction.getStatus()));
        transfer.setCurrency(transferOut.getQuoteCurrency());
        transfer.setQuoteCurrency(transferOut.getCurrency());
        transfer.setAccountId(transferOut.getAccountId());
        transfer.setOriginAmount(amount.add(fee));
        transfer.setTransactionId(transaction.getId());

        transfer.setSenderType(CounterpartyType.WIRE);
        transfer.setRecipientType(CounterpartyType.WALLET);
        transfer.setTransactionTime(entity.getCreateTime());
        transfer.setDisplayStatus(transaction.getStatus());
        transfer.setRelatedQbitTxId(transactionOut.getTransactionDisplayId());
        // 完成订单落库
        this.save(transfer);
        return transfer;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public CryptoAssetsTransfer hook(CryptoAssetsTransaction entity) {
        return null;
    }

    /**
     * 转出到链上
     *
     * @param params {@link CryptoAssetsTransferDTO}
     * @return true/false
     */
    @Transactional
    public CryptoAssetsTransfer transferToChain(CryptoAssetsTransferDTO params) {
        BigDecimal amount = BigDecimal.valueOf(params.getAmount());
        Balance exist = balanceService.getById(params.getFromBalanceId());
        List<WalletTypeEnum> wallets = Arrays.asList(WalletTypeEnum.CircleWallet, WalletTypeEnum.OkxWallet);
        if (!wallets.contains(exist.getWalletType())) {
            throw new CustomException("当前钱包不支持");
        }
        ChainType chain = params.getChain();
        // 交易手续费默认为0
        BigDecimal fee = new BigDecimal("0.08");
        //withdrawalService.getFee(exist, chain);
        BigDecimal settleAmount = amount.add(fee);
        // 检查钱包和金额
        Balance balance = balanceService.checkBalanceAmountWithError(params.getFromBalanceId(), settleAmount, BalanceColumnTypeEnum.Available);
        // 验证三方提币限额
        //withdrawalService.verifyQuota(balance, chain, settleAmount);
        // 创建USDC交易订单
        CryptoAssetsTransfer transfer = new CryptoAssetsTransfer();
        transfer.setId(UUID.randomUUID().toString());
        transfer.setBalanceId(params.getFromBalanceId());
        transfer.setAction(CryptoAssetsTransferAction.OUT);
        transfer.setChain(chain);
        transfer.setAddress(params.getToAddress());
        transfer.setSettlementAmount(amount);
        transfer.setAccountId(balance.getAccountId());
        transfer.setOriginAmount(settleAmount);
        transfer.setFee(fee);
        transfer.setStatus(Processing);
        transfer.setDisplayStatus(TransactionStatusEnum.Pending);
        transfer.setQuoteCurrency(balance.getCurrency());
        transfer.setSenderType(CounterpartyType.WALLET);
        transfer.setRecipientType(CounterpartyType.CHAIN);
        transfer.setCurrency(balance.getCurrency());
        //transfer.setPayeeId(params.getDestination().getPayeeId());
        // 创建变动钱的订单
        BalanceChangeDTO dto = new BalanceChangeDTO();
        dto.setBalanceId(balance.getId());
        dto.setCurrency(balance.getCurrency());
        dto.setAccountId(balance.getAccountId());
        dto.setFee(fee);
        dto.setCost(amount);
        dto.setSourceId(transfer.getId());
        dto.setOperation(BalanceOperationEnum.Sub);
        dto.setType(TransactionTypeEnum.CryptoAssetsTransferOut);
        dto.setSourceType(TransactionSourceTypeEnum.QbitCrypto);
        Transaction transaction = transactionService.singleBalanceSubAmountToPendingV2(dto);
        transfer.setTransactionId(transaction.getId());
        this.save(transfer);
        return transfer;
    }


    /**
     * 获取前端显示状态
     *
     * @param status status
     * @return new status
     */
    private TransactionStatusEnum setDisplayStatusAction(CryptoAssetsTransferStatus status) {
        if (status.equals(Closed)) {
            return TransactionStatusEnum.Closed;
        } else if (status.equals(Cancelled)) {
            return TransactionStatusEnum.Fail;
        } else {
            return TransactionStatusEnum.Pending;
        }
    }

    private CryptoAssetsTransfer getByTradeId(String tradeId) {
        LambdaQueryWrapper<CryptoAssetsTransfer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CryptoAssetsTransfer::getTradeId, tradeId).last("LIMIT 1");
        return this.getOne(wrapper);
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

    /**
     * 处理fee, circle 那边会更改数据结构
     *
     * @param data circle fee数据
     * @return {@link List<AmountBO>}
     */
    private List<AmountBO> getFees(Object data) {
        List<AmountBO> fees = new ArrayList<>();
        if (data instanceof Map<?, ?>) {
            AmountBO fee = JsonUtil.toBean(data, AmountBO.class);
            if (fee != null) {
                fees.add(fee);
            }
        } else if (data instanceof List<?>) {
            JavaType javaType = JsonUtil.getCollectionType(List.class, AmountBO.class);
            List<AmountBO> list = JsonUtil.toBean(JsonUtil.toJSONString(data), javaType);
            if (list != null) {
                fees.addAll(list);
            }
        }
        return fees;
    }
}
