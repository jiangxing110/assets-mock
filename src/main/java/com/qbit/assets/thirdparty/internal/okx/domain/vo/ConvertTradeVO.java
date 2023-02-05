package com.qbit.assets.thirdparty.internal.okx.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 闪兑交易
 */
@Data
public class ConvertTradeVO implements Serializable {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 成交ID
     */
    private String tradeId;

    /**
     * 报价ID
     */
    private String quoteId;

    /**
     * 用户自定义的订单标识
     */
    private String clTReqId;

    /**
     * fullyFilled：交易成功 rejected：交易失败
     */
    private String state;

    /**
     * 币对，如 BTC-USDT
     */
    private String instId;

    /**
     * 交易货币币种，如 BTC-USDT中BTC
     */
    private String baseCcy;

    /**
     * 计价货币币种，如 BTC-USDT中USDT
     */
    private String quoteCcy;

    /**
     * 交易方向 买：buy 卖：sell
     */
    private String side;

    /**
     * 成交价格，单位为计价币
     */
    private String fillPx;

    /**
     * 成交的交易币数量
     */
    private String fillBaseSz;

    /**
     * 成交的计价币数量
     */
    private String fillQuoteSz;

    /**
     * 闪兑交易时间，值为时间戳，Unix时间戳为毫秒数格式，如 1597026383085
     */
    private String ts;
}
