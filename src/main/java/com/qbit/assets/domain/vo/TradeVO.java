package com.qbit.assets.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author martinjiang
 * @description TradeVO
 * @date 2023/2/19 01:25
 */
@Data
public class TradeVO implements Serializable {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 三方交易id
     */
    private String id;

    /**
     * 自定义交易id
     */
    private String tradeId;

    /**
     * 交易状态
     * <p>
     * complete：交易成功
     * failed：交易失败
     */
    private String state;

    /**
     * 交易货币币种，如 BTC-USDT 中BTC
     */
    private String baseCcy;

    /**
     * 计价货币币种，如 BTC-USDT 中USDT
     */
    private String quoteCcy;

    /**
     * 交易方向
     * 买：buy 卖：sell
     */
    private String side;

    /**
     * 成交价格
     */
    private BigDecimal finalPrice;

    /**
     * 成交的交易币数量
     */
    private BigDecimal finalBaseSz;

    /**
     * 成交的计价币数量
     */
    private BigDecimal finalQuoteSz;

    /**
     * 闪兑交易时间，值为时间戳，Unix时间戳为毫秒数格式，如 1597026383085
     */
    private Long timestamp;
}