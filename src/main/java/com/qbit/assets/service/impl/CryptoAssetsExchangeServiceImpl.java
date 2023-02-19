package com.qbit.assets.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qbit.assets.common.enums.*;
import com.qbit.assets.common.error.CustomException;
import com.qbit.assets.common.utils.AmountUtil;
import com.qbit.assets.common.utils.HexUtil;
import com.qbit.assets.common.utils.JsonUtil;
import com.qbit.assets.common.utils.ValidatorUtil;
import com.qbit.assets.domain.dto.AssetExchangeDto;
import com.qbit.assets.domain.dto.BalanceChangeDTO;
import com.qbit.assets.domain.entity.*;
import com.qbit.assets.domain.vo.AssetsTransferVo;
import com.qbit.assets.domain.vo.CryptoAssetsTransferVO;
import com.qbit.assets.domain.vo.TradeVO;
import com.qbit.assets.mapper.EstimateQuotesMapper;
import com.qbit.assets.service.*;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.ConvertCurrencyPairVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.*;

/**
 * @author litao
 */
@Slf4j
@Service
public class CryptoAssetsExchangeServiceImpl implements CryptoAssetsExchangeService {
    @Resource
    private BalanceService balanceService;

    @Resource
    private CryptoAssetsTransferService transferService;

    @Resource
    private TransactionService transactionService;
    @Resource
    private CurrenciesPairsService currenciesPairsService;
    @Resource
    private EstimateQuotesService estimateQuoteService;
    @Resource
    private EstimateQuotesMapper estimateQuotesMapper;
    @Resource
    private CryptoAssetsTradeService cryptoAssetsTradeService;


    /**
     * 创建买单交易
     */
    @Override
    public CryptoAssetsTransfer createBuyTransfer(String tradeId, Balance balance, BigDecimal amount, BigDecimal fee, BigDecimal rate, CryptoConversionCurrencyEnum quoteCurrency) {
        return createExchangeTransfer(tradeId, balance, amount, fee, rate, quoteCurrency, CryptoAssetsTransferAction.BUY);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void fail(CryptoAssetsTransfer transfer, String tradeId) {
        Transaction transaction = transactionService.updateBalanceByBalanceTransactionIdV2(transfer.getTransactionId(), TransactionStatusEnum.Fail);
        transfer.setTradeId(tradeId);
        handleStatusMapping(transaction, transfer);
        transferService.updateById(transfer);
    }

    /**
     * @param sell
     * @param balance
     * @param amount
     * @param trade
     * @throws ParseException
     */
    @Override
    public void complete(CryptoAssetsTransfer sell, Balance balance, BigDecimal amount, TradeVO trade) throws ParseException {
        String tradeId = trade.getId();
        BigDecimal buyRate;
        BigDecimal sellRate;
        int precision = AmountUtil.getPrecision(
                CryptoConversionCurrencyEnum.getItem(trade.getBaseCcy()),
                CryptoConversionCurrencyEnum.getItem(trade.getQuoteCcy()),
                sell.getCurrency()
        );
        if (StringUtils.equals(trade.getBaseCcy(), sell.getCurrency().getValue())) {
            sellRate = trade.getFinalPrice().setScale(precision, RoundingMode.FLOOR);
            buyRate = BigDecimal.ONE.divide(sellRate, 8, RoundingMode.FLOOR).setScale(precision, RoundingMode.FLOOR);
        } else {
            buyRate = trade.getFinalPrice().setScale(precision, RoundingMode.FLOOR);
            sellRate = BigDecimal.ONE.divide(buyRate, 8, RoundingMode.FLOOR).setScale(precision, RoundingMode.FLOOR);
        }
        CryptoAssetsTransfer buy = handleBuyTransfer(tradeId, balance, amount, buyRate, sell.getCurrency());
        // 卖单同步买单的rate, trade_id字段
        sell.setRate(sellRate);
        sell.setTradeId(buy.getTradeId());
        complete(sell);
        complete(buy);
        //this.partnerOrderService.createPartnerOrder(sell);
    }

    private CryptoAssetsTransfer handleBuyTransfer(String tradeId, Balance balance, BigDecimal amount, BigDecimal rate, CryptoConversionCurrencyEnum quoteCurrency) {
        LambdaQueryWrapper<CryptoAssetsTransfer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CryptoAssetsTransfer::getAction, CryptoAssetsTransferAction.BUY);
        wrapper.eq(CryptoAssetsTransfer::getTradeId, tradeId);

        CryptoAssetsTransfer buyTransfer = transferService.getOne(wrapper);

        if (buyTransfer == null) {
            // 构建买单交易
            buyTransfer = createBuyTransfer(
                    tradeId,
                    balance,
                    amount,
                    BigDecimal.ZERO,
                    rate,
                    quoteCurrency
            );
        }
        return buyTransfer;
    }

    private void complete(CryptoAssetsTransfer transfer) {
        Transaction transaction = transactionService.updateBalanceByBalanceTransactionIdV2(transfer.getTransactionId(), TransactionStatusEnum.Closed);
        handleStatusMapping(transaction, transfer);
        transferService.updateById(transfer);
    }

    /**
     * 创建卖单交易
     */
    @Override
    public CryptoAssetsTransfer createSellTransfer(String tradeId, Balance balance, BigDecimal amount, BigDecimal fee, CryptoConversionCurrencyEnum quoteCurrency) {
        return createExchangeTransfer(tradeId, balance, amount, fee, BigDecimal.ZERO, quoteCurrency, CryptoAssetsTransferAction.SELL);
    }

    private CryptoAssetsTransfer createExchangeTransfer(String tradeId, Balance balance, BigDecimal amount, BigDecimal fee, BigDecimal rate, CryptoConversionCurrencyEnum quoteCurrency, CryptoAssetsTransferAction action) {
        // 构建处理中的卖单
        BigDecimal originAmount = amount;
        BigDecimal settlementAmount = amount.subtract(fee);
        BalanceOperationEnum operation = BalanceOperationEnum.Sub;
        if (action == CryptoAssetsTransferAction.BUY) {
            operation = BalanceOperationEnum.Add;
        }
        // 构建钱包加/减钱参数
        BalanceChangeDTO params = transferService.buildTransferDTO(balance, settlementAmount, fee, operation);
        Transaction transaction;
        if (action == CryptoAssetsTransferAction.BUY) {
            transaction = transactionService.singleBalanceAddAmountToPendingV2(params);
        } else {
            params.setFee(fee);
            params.setCost(settlementAmount);
            transaction = transactionService.singleBalanceSubAmountToPendingV2(params);
        }
        // 构建加密资产交易订单
        CryptoAssetsTransfer transfer = buildTransfer(action, transaction);
        transfer.setStatus(CryptoAssetsTransferStatus.Processing);
        transfer.setDisplayStatus(TransactionStatusEnum.Pending);
        transfer.setBalanceId(balance.getId());
        transfer.setTradeId(tradeId);
        transfer.setOriginAmount(originAmount);
        transfer.setSettlementAmount(settlementAmount);
        transfer.setFee(fee);
        transfer.setRate(rate);
        transfer.setQuoteCurrency(quoteCurrency);
        transferService.save(transfer);
        return transfer;
    }

    @Override
    public CryptoAssetsTransfer buildTransfer(CryptoAssetsTransferAction action, Transaction transaction) {
        CryptoAssetsTransfer transfer = new CryptoAssetsTransfer();
        transfer.setAction(action);
        transfer.setSettlementAmount(transaction.getSenderCost());
        transfer.setTransactionId(transaction.getId());
        transfer.setTransactionTime(transaction.getTransactionTime());
        transfer.setAccountId(transaction.getAccountId());
        transfer.setRate(BigDecimal.ONE);
        transfer.setChain(ChainType.NA);

        handleStatusMapping(transaction, transfer);
        transfer.setSenderType(CounterpartyType.WALLET);
        transfer.setRecipientType(CounterpartyType.WALLET);
        if (action == CryptoAssetsTransferAction.BUY) {
            transfer.setOriginAmount(transaction.getSenderCost());
            transfer.setFee(BigDecimal.ZERO);
            transfer.setCurrency(transaction.getRecipientCurrency());
            transfer.setBalanceId(transaction.getRecipientBalanceId());
            transfer.setQuoteCurrency(transaction.getSenderCurrency());
        } else {
            transfer.setOriginAmount(transaction.getSenderCost().add(transaction.getSenderFee()));
            //处理加点费率
            //BigDecimal fee2 = this.getFee2(transaction.getAccountId(), transaction.getSenderCurrency(), transaction.getRecipientCurrency(), transfer.getOriginAmount());
            transfer.setFee(transaction.getSenderFee());
            //transfer.setFee2(fee2);
            transfer.setCurrency(transaction.getSenderCurrency());
            transfer.setBalanceId(transaction.getSenderBalanceId());
            transfer.setQuoteCurrency(transaction.getRecipientCurrency());
        }
        return transfer;
    }

    /**
     * 闪兑交易
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public AssetsTransferVo trade(AssetExchangeDto body) {
        AssetsTransferVo transferVo = new AssetsTransferVo();
        // 原始金额
        BigDecimal originAmount = body.getAmount();
        String quoteId = body.getQuoteId();
        // 1. 检查源币种/目标币种是否存在
        Balance balance = balanceService.checkBalance(body.getSource());
        Balance targetBalance = balanceService.checkBalance(body.getTarget());
        // 检查币对限额
        //checkCurrencyPairQuota(balance.getCurrency(), targetBalance.getCurrency(), balance.getCurrency(), body.getAmount());
        BigDecimal fee = body.getFee();
        BigDecimal amount = originAmount.subtract(fee);
        // 2. 检查源币种资金是否充足
        boolean flag = balanceService.checkBalanceAmount(balance.getId(), originAmount, BalanceColumnTypeEnum.Available);
        if (!flag) {
            throw new CustomException("资金不足");
        }
        CryptoAssetsTransfer transfer = this.convert(balance, targetBalance, amount, fee, quoteId);
        log.info("闪兑交易: id: {}, trade id: {}", transfer.getId(), transfer.getTradeId());
        CryptoAssetsTransferVO vo = new CryptoAssetsTransferVO();
        BeanUtils.copyProperties(transfer, vo);
        // 关联买卖订单
        LambdaQueryWrapper<CryptoAssetsTransfer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CryptoAssetsTransfer::getAction, CryptoAssetsTransferAction.BUY)
                .eq(CryptoAssetsTransfer::getTradeId, transfer.getTradeId()).last("LIMIT 1");
        CryptoAssetsTransfer buy = transferService.getOne(wrapper);
        if (buy != null) {
            CryptoAssetsTransferVO transferBuy = new CryptoAssetsTransferVO();
            BeanUtils.copyProperties(buy, transferBuy);
            //vo.setAssociatedOrder(transferBuy);
        }
        return transferVo;
    }


    @Override
    public CryptoAssetsTransfer convert(Balance fromBalance, Balance toBalance, BigDecimal amount, BigDecimal fee, String quoteId) {
        // 获取滑点(暗点)费
        BigDecimal slipDotFee = new BigDecimal("1");
        // 检查询价信息
        EstimateQuotes estimateQuote = checkEstimateQuote(fromBalance.getCurrency(), toBalance.getCurrency(), amount.subtract(slipDotFee), quoteId);
        // 构建处理中的卖单
        CryptoAssetsTransfer transfer = this.createSellTransfer(SpecialUUID.NullUUID.value(), fromBalance, amount.add(fee), fee, toBalance.getCurrency());
        // 记录询价id
        Map<String, String> rawData = new HashMap<>(2);
        rawData.put("quoteId", UUID.randomUUID().toString());
        transfer.setRawData(JsonUtil.toJSONString(rawData));
        // 请求三方完成交易
        TradeVO vo = trade(transfer, estimateQuote);
        CryptoAssetsTrade trade = buildTrade(transfer.getAccountId(), fromBalance.getCurrency(), fee, fromBalance.getCurrency(), slipDotFee, estimateQuote, vo, transfer.getId());
        cryptoAssetsTradeService.save(trade);
        // 构建买单
        amount = vo.getFinalBaseSz();
        if (vo.getQuoteCcy().equals(toBalance.getCurrency().getValue())) {
            amount = vo.getFinalQuoteSz();
        }
        if (StringUtils.equals(vo.getState(), "complete")) {
            try {
                // 同时关闭 卖单/买单 交易
                this.complete(transfer, toBalance, amount, vo);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            this.fail(transfer, vo.getId());
        }
        return transfer;
    }

    private CryptoAssetsTrade buildTrade(String accountId,
                                         CryptoConversionCurrencyEnum feeCurrency,
                                         BigDecimal feeAmount,
                                         CryptoConversionCurrencyEnum slippageCurrency,
                                         BigDecimal slippageAmount,
                                         EstimateQuotes estimateQuote,
                                         TradeVO vo,
                                         String clientId) {
        CryptoAssetsTrade trade = new CryptoAssetsTrade();
        trade.setAccountId(accountId);
        trade.setQuoteId(estimateQuote.getId());
        String symbol = String.format("%s-%s", estimateQuote.getBaseCurrency().getValue(), estimateQuote.getQuoteCurrency().getValue());
        trade.setSymbol(symbol);
        trade.setSide(estimateQuote.getSide());
        trade.setBaseCurrency(estimateQuote.getBaseCurrency());
        trade.setBaseAmount(vo.getFinalBaseSz());
        trade.setQuoteCurrency(estimateQuote.getQuoteCurrency());
        trade.setQuoteAmount(vo.getFinalQuoteSz());
        trade.setFeeCurrency(feeCurrency);
        trade.setFeeAmount(feeAmount);
        trade.setSlippageCurrency(slippageCurrency);
        trade.setSlippageAmount(slippageAmount);
        trade.setRate(estimateQuote.getRate());
        trade.setTradeId(vo.getId());
        trade.setClientId(clientId);

        if (StringUtils.equals(vo.getState(), "complete")) {
            trade.setStatus(TransactionStatusEnum.Closed);
        } else {
            trade.setStatus(TransactionStatusEnum.Fail);
        }

        return trade;
    }

    private EstimateQuotes checkEstimateQuote(CryptoConversionCurrencyEnum baseCurrency, CryptoConversionCurrencyEnum quoteCurrency, BigDecimal amount, String quoteId) {
        CurrenciesPairs pair = this.getCurrencyPair(baseCurrency, quoteCurrency);
        CryptoAssetsTransferAction side = null;
        if (pair.getBaseCurrency() == baseCurrency) {
            side = CryptoAssetsTransferAction.SELL;
        } else if (pair.getQuoteCurrency() == baseCurrency) {
            side = CryptoAssetsTransferAction.BUY;
        }
        EstimateQuotes estimateQuote;
        // 传入一个不合法的询价id, 默认请求一个新的询价信息
        if (!ValidatorUtil.isUUID(quoteId)) {
            estimateQuote = getEstimateQuote(baseCurrency, quoteCurrency, baseCurrency, amount, side, null);
        }
        estimateQuote = estimateQuotesMapper.selectById(quoteId);
        // 询价信息不在数据库中, 默认请求个新的
        if (estimateQuote == null) {
            estimateQuote = getEstimateQuote(baseCurrency, quoteCurrency, baseCurrency, amount, side, null);
        }

        // 获取交易金额
        BigDecimal baseAmount = estimateQuote.getBaseAmount();
        BigDecimal quoteAmount = estimateQuote.getQuoteAmount();

        // 金额精度
        int basePrecision = AmountUtil.getPrecision(estimateQuote.getBaseCurrency());
        int quotePrecision = AmountUtil.getPrecision(estimateQuote.getQuoteCurrency());

        // 精度在手续费计算的时候进位不同, 这里比对数据的时候允许有误差
        BigDecimal baseAcceptDiff = new BigDecimal("2").divide(BigDecimal.TEN.pow(basePrecision), basePrecision, RoundingMode.FLOOR);
        BigDecimal quoteAcceptDiff = new BigDecimal("2").divide(BigDecimal.TEN.pow(quotePrecision), quotePrecision, RoundingMode.FLOOR);

        // 检查询价信息和交易信息是否一致
        BigDecimal baseDiff = baseAmount.subtract(amount).abs();
        BigDecimal quoteDiff = quoteAmount.subtract(amount).abs();
        // 稳定币汇率接近, 可能会导致交易两个币种都在阈值范围内
        if (baseDiff.compareTo(baseAcceptDiff) > 0 && quoteDiff.compareTo(quoteAcceptDiff) > 0) {
            throw new CustomException("EXCHANGE_40001");
        }


        long timestamp = estimateQuote.getCreateTime().getTime() + estimateQuote.getTtlMs();
        long currentTimeMillis = System.currentTimeMillis();
        // 三方有效期是10秒, 我方为保证正常交易9秒内允许提交
        // 保留1秒的网络请求时间

        return estimateQuote;
    }

    private EstimateQuotes getEstimateQuote(CryptoConversionCurrencyEnum baseCurrency, CryptoConversionCurrencyEnum quoteCurrency, CryptoConversionCurrencyEnum baseCurrency1, BigDecimal amount, CryptoAssetsTransferAction side, Object o) {
        EstimateQuotes data = new EstimateQuotes();
        ConvertCurrencyPairVO pair = getConvertCurrencyPair();

        data.setQuoteId(UUID.randomUUID().toString());
        data.setBaseCurrency(pair.getBaseCcy());
        data.setBaseAmount(amount);
        data.setQuoteCurrency(pair.getQuoteCcy());
        data.setQuoteAmount(amount);
        data.setSide(side);

        data.setRfqAmount(amount);
        data.setRfqCurrency(baseCurrency);

        data.setRate(BigDecimal.ONE);
        data.setCreateTime(new Date());
        data.setTtlMs(-1L);
        return data;
    }

    private CurrenciesPairs getCurrencyPair(CryptoConversionCurrencyEnum fromCurrency, CryptoConversionCurrencyEnum toCurrency) {
        LambdaQueryWrapper<CurrenciesPairs> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CurrenciesPairs::getBaseCurrency, fromCurrency, toCurrency);
        wrapper.in(CurrenciesPairs::getQuoteCurrency, fromCurrency, toCurrency);
        CurrenciesPairs pair = currenciesPairsService.getOne(wrapper);
        return pair;
    }


    /**
     * 调用三方交易
     *
     * @param transfer      {@link CryptoAssetsTransfer}
     * @param estimateQuote {@link EstimateQuotes}
     * @return {@link TradeVO}
     */
    private TradeVO trade(CryptoAssetsTransfer transfer, EstimateQuotes estimateQuote) {
        String clientId = HexUtil.UUIDToHex(transfer.getId());
        // 交易金额取锁汇信息中的请求金额
        // 锁汇中的信息已经扣除了fee2
        BigDecimal amount = estimateQuote.getRfqAmount();

        // 请求三方完成交易
        TradeVO vo = completeTrade(transfer.getCurrency().getValue(), transfer.getQuoteCurrency().getValue(), amount, clientId, estimateQuote);

        // 设置我方数据精度
        int precision = AmountUtil.getPrecision(Objects.requireNonNull(CryptoConversionCurrencyEnum.getItem(vo.getBaseCcy())));
        BigDecimal finalBaseSz = vo.getFinalBaseSz().setScale(precision, RoundingMode.FLOOR);
        vo.setFinalBaseSz(finalBaseSz);

        precision = AmountUtil.getPrecision(Objects.requireNonNull(CryptoConversionCurrencyEnum.getItem(vo.getQuoteCcy())));
        BigDecimal finalQuoteSz = vo.getFinalQuoteSz().setScale(precision, RoundingMode.FLOOR);
        vo.setFinalQuoteSz(finalQuoteSz);
        vo.setFinalPrice(estimateQuote.getRate());
        return vo;
    }

    private ConvertCurrencyPairVO getConvertCurrencyPair() {
        ConvertCurrencyPairVO pair = new ConvertCurrencyPairVO();
        pair.setInstId("USDC-USD");
        pair.setBaseCcy(CryptoConversionCurrencyEnum.USDC);
        pair.setBaseCcyMax("500000");
        pair.setBaseCcyMin("10");
        pair.setQuoteCcy(CryptoConversionCurrencyEnum.USD);
        pair.setQuoteCcyMax("500000");
        pair.setQuoteCcyMin("10");
        return pair;
    }

    public TradeVO completeTrade(String baseCcy, String quoteCcy, BigDecimal amount, String tradeId, EstimateQuotes estimateQuote) {
        TradeVO vo = new TradeVO();
        vo.setId(UUID.randomUUID().toString());
        vo.setTradeId(tradeId);
        vo.setState("complete");
        vo.setBaseCcy(baseCcy);
        vo.setQuoteCcy(quoteCcy);
        vo.setSide(estimateQuote.getSide().getValue());
        vo.setFinalPrice(estimateQuote.getRate());
        vo.setFinalBaseSz(estimateQuote.getBaseAmount());
        vo.setFinalQuoteSz(estimateQuote.getQuoteAmount());
        vo.setTimestamp(System.currentTimeMillis());
        return vo;
    }

    private void handleStatusMapping(Transaction transaction, CryptoAssetsTransfer transfer) {
        switch (transaction.getStatus()) {
            case Closed -> {
                transfer.setStatus(CryptoAssetsTransferStatus.Closed);
                transfer.setDisplayStatus(TransactionStatusEnum.Closed);
            }
            case Fail -> {
                transfer.setStatus(CryptoAssetsTransferStatus.Cancelled);
                transfer.setDisplayStatus(TransactionStatusEnum.Fail);
            }
            default -> {
                transfer.setStatus(CryptoAssetsTransferStatus.Processing);
                transfer.setDisplayStatus(TransactionStatusEnum.Pending);
            }
        }
    }
}
