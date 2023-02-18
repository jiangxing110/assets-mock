package com.qbit.assets.thirdparty.internal.okx.domain.vo;

import com.qbit.assets.common.enums.CryptoAssetsTransferAction;
import com.qbit.assets.common.enums.CryptoConversionCurrencyEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 闪兑预估询价信息
 *
 * @author litao
 */
@Data
public class ConvertEstimateQuoteVO implements Serializable {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 生成报价时间，Unix时间戳的毫秒数格式
     */
    private String quoteTime;

    /**
     * 报价有效期，单位为毫秒
     */
    private String ttlMs;

    /**
     * 客户端自定义的订单标识
     */
    private String clQReqId;

    /**
     * 报价ID
     */
    private String quoteId;

    /**
     * 交易货币币种，如 BTC-USDT 中BTC
     */
    private CryptoConversionCurrencyEnum baseCcy;

    /**
     * 计价货币币种，如 BTC-USDT 中USDT
     */
    private CryptoConversionCurrencyEnum quoteCcy;

    /**
     * 交易方向
     * 买：buy 卖：sell
     */
    private CryptoAssetsTransferAction side;

    /**
     * 原始报价的数量
     */
    private String origRfqSz;

    /**
     * 实际报价的数量
     */
    private String rfqSz;

    /**
     * 报价的币种
     */
    private CryptoConversionCurrencyEnum rfqSzCcy;

    /**
     * 闪兑价格，单位为计价币
     */
    private String cnvtPx;

    /**
     * 闪兑交易币数量
     */
    private String baseSz;

    /**
     * 闪兑计价币数量
     */
    private String quoteSz;
}
