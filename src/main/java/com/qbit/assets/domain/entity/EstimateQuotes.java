package com.qbit.assets.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.domain.base.BaseV2;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assets_estimate_quotes")
public class EstimateQuotes extends BaseV2 {

    private static final long serialVersionUID = 1L;

    /**
     * 询价id
     */
    private String quoteId;

    /**
     * 交易币
     */
    private String baseCurrency;

    /**
     * 交易币数量
     */
    private BigDecimal baseAmount;

    /**
     * 计价币
     */
    private String quoteCurrency;

    /**
     * 计价币数量
     */
    private BigDecimal quoteAmount;

    /**
     * 交易方向(买：buy 卖：sell)
     */
    private String side;

    /**
     * 汇率
     */
    private BigDecimal rate;

    /**
     * 计价有效期(毫秒)
     */
    private Long ttlMs;

    /**
     * 询价数量
     */
    private BigDecimal rfqAmount;

    /**
     * 询价币种
     */
    private String rfqCurrency;

    /**
     * 原始汇率
     */
    private BigDecimal originRate;


}
