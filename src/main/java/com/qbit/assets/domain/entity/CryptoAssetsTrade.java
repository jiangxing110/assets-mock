package com.qbit.assets.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.common.enums.CryptoAssetsTransferAction;
import com.qbit.assets.common.enums.CryptoConversionCurrencyEnum;
import com.qbit.assets.common.enums.TransactionStatusEnum;
import com.qbit.assets.domain.base.BaseV2;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;


/**
 * @author EDZ
 * @description CryptoAssetsTrade
 * @date 2022/11/15 13:43
 */
@TableName("assets_trade")
@EqualsAndHashCode(callSuper = true)
@Data
public class CryptoAssetsTrade extends BaseV2 {
    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * accountId
     */
    private String accountId;

    /**
     * 询价id
     */
    private String quoteId;

    /**
     * 交易对
     */
    private String symbol;

    /**
     * 交易方向
     * sell: 卖
     * buy: 买
     */
    private CryptoAssetsTransferAction side;

    /**
     * 交易币
     */
    private CryptoConversionCurrencyEnum baseCurrency;

    /**
     * 交易币数量
     */
    private BigDecimal baseAmount;

    /**
     * 计价币
     */
    private CryptoConversionCurrencyEnum quoteCurrency;

    /**
     * 计价币数量
     */
    private BigDecimal quoteAmount;

    /**
     * 手续费币种
     */
    private CryptoConversionCurrencyEnum feeCurrency;

    /**
     * 手续费数量
     */
    private BigDecimal feeAmount;

    /**
     * 滑点币种
     */
    private CryptoConversionCurrencyEnum slippageCurrency;

    /**
     * 滑点数量
     */
    private BigDecimal slippageAmount;

    /**
     * 成交汇率 单位计价币
     */
    private BigDecimal rate;

    /**
     * 三方交易id
     */
    private String tradeId;

    /**
     * 自定义id
     */
    private String clientId;

    /**
     * 交易备注
     */
    private String remark;

    /**
     * 交易状态
     */
    private TransactionStatusEnum status;
}
