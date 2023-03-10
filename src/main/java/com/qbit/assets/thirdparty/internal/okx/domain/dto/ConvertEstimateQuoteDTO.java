package com.qbit.assets.thirdparty.internal.okx.domain.dto;


import com.qbit.assets.common.enums.CryptoAssetsTransferAction;
import com.qbit.assets.common.enums.CryptoConversionCurrencyEnum;
import com.qbit.assets.thirdparty.internal.okx.annotation.ITag;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 闪兑预估询价
 *
 * @author litao
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConvertEstimateQuoteDTO extends BaseDTO implements ITag {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 交易货币币种，如 BTC-USDT中的BTC
     */
    private CryptoConversionCurrencyEnum baseCcy;

    /**
     * 计价货币币种，如 BTC-USDT中的USDT
     */
    private CryptoConversionCurrencyEnum quoteCcy;

    /**
     * 交易方向
     * 买：buy 卖：sell
     * 描述的是对于baseCcy的交易方向
     */
    private CryptoAssetsTransferAction side;

    /**
     * 询价数量
     */
    private String rfqSz;

    /**
     * 询价币种
     */
    private CryptoConversionCurrencyEnum rfqSzCcy;

    /**
     * 客户端自定义的订单标识
     * 字母（区分大小写）与数字的组合，可以是纯字母、纯数字且长度要在1-32位之间。
     */
    private String clQReqId;

    /**
     * 订单标签
     */
    private String tag;
}
