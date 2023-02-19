package com.qbit.assets.service;

import com.qbit.assets.common.enums.CryptoAssetsTransferAction;
import com.qbit.assets.common.enums.CryptoConversionCurrencyEnum;
import com.qbit.assets.domain.dto.AssetExchangeDto;
import com.qbit.assets.domain.entity.Balance;
import com.qbit.assets.domain.entity.CryptoAssetsTransfer;
import com.qbit.assets.domain.entity.Transaction;
import com.qbit.assets.domain.vo.AssetsTransferVo;
import com.qbit.assets.domain.vo.TradeVO;

import java.math.BigDecimal;
import java.text.ParseException;

/**
 * @author litao
 */
public interface CryptoAssetsExchangeService {
    /**
     * 创建卖单交易
     */
    CryptoAssetsTransfer createSellTransfer(String tradeId, Balance balance, BigDecimal amount, BigDecimal fee, CryptoConversionCurrencyEnum quoteCurrency);

    /**
     * 创建买单交易
     */
    CryptoAssetsTransfer createBuyTransfer(String tradeId, Balance balance, BigDecimal amount, BigDecimal fee, BigDecimal rate, CryptoConversionCurrencyEnum quoteCurrency);

    /**
     * 完成订单
     */
    void fail(CryptoAssetsTransfer sell, String tradeId);

    /**
     * 完成订单
     */
    void complete(CryptoAssetsTransfer sell, Balance balance, BigDecimal amount, TradeVO trade) throws ParseException;

    /**
     * 根据transaction构建单条加密资产交易记录 transfer entity
     *
     * @param action      action
     * @param transaction transaction
     * @return transfer
     */
    CryptoAssetsTransfer buildTransfer(CryptoAssetsTransferAction action, Transaction transaction);

    /**
     * 闪兑交易
     */
    AssetsTransferVo trade(AssetExchangeDto body);

    /**
     * 闪兑交易
     */
    CryptoAssetsTransfer convert(Balance fromBalance, Balance toBalance, BigDecimal amount, BigDecimal fee, String quoteId);


}
