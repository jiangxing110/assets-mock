package com.qbit.assets.thirdparty.internal.okx.domain.dto;


import com.qbit.assets.thirdparty.internal.okx.annotation.ITag;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 闪兑交易
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConvertTradeDTO extends BaseDTO implements ITag {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 报价ID
     */
    private String quoteId;

    /**
     * 交易货币币种，如 BTC-USDT中的BTC
     */
    private String baseCcy;

    /**
     * 计价货币币种，如 BTC-USDT中的USDT
     */
    private String quoteCcy;

    /**
     * 交易方向
     * 买：buy 卖：sell
     * 描述的是对于baseCcy的交易方向
     */
    private String side;

    /**
     * 用户报价数量
     * 报价数量应不大于预估询价中的询价数量
     */
    private String sz;

    /**
     * 用户报价币种
     */
    private String szCcy;

    /**
     * 用户自定义的订单标识
     * 字母（区分大小写）与数字的组合，可以是纯字母、纯数字且长度要在1-32位之间。
     */
    private String clTReqId;

    /**
     * 订单标签
     * 字母（区分大小写）与数字的组合，可以是纯字母、纯数字且长度要在1-32位之间。
     */
    private String tag;

}
