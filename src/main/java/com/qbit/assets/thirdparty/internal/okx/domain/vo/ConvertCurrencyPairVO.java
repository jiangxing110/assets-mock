package com.qbit.assets.thirdparty.internal.okx.domain.vo;

import com.qbit.assets.common.enums.CryptoConversionCurrencyEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * okx 闪兑币对信息
 *
 * @author litao
 */
@Data
public class ConvertCurrencyPairVO implements Serializable {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 币对，如 BTC-USDT
     */
    private String instId;

    /**
     * 交易货币币种，如 BTC-USDT中的BTC
     */
    private CryptoConversionCurrencyEnum baseCcy;

    /**
     * 交易货币支持闪兑的最大值
     */
    private String baseCcyMax;

    /**
     * 交易货币支持闪兑的最小值
     */
    private String baseCcyMin;

    /**
     * 计价货币币种，如 BTC-USDT中的USDT
     */
    private CryptoConversionCurrencyEnum quoteCcy;

    /**
     * 计价货币支持闪兑的最大值
     */
    private String quoteCcyMax;

    /**
     * 计价货币支持闪兑的最小值
     */
    private String quoteCcyMin;

}
