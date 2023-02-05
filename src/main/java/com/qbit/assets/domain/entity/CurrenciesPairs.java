package com.qbit.assets.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.domain.base.BaseV2;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assets_currencies_pairs")
public class CurrenciesPairs extends BaseV2 {

    private static final long serialVersionUID = 1L;

    /**
     * 交易对
     */
    private String symbol;

    /**
     * 交易货币币种，如 BTC-USDT中的BTC
     */
    private String baseCurrency;

    /**
     * 交易货币支持的最小值
     */
    private BigDecimal baseMin;

    /**
     * 交易货币支持的最大值
     */
    private BigDecimal baseMax;

    /**
     * 计价货币币种，如 BTC-USDT中的USDT
     */
    private String quoteCurrency;

    /**
     * 计价货币支持的最小值
     */
    private BigDecimal quoteMin;

    /**
     * 计价货币支持的最大值
     */
    private BigDecimal quoteMax;


}
