package com.qbit.assets.thirdparty.internal.okx.domain.dto;

import lombok.Data;

/**
 * OKX 子账户充值查询
 *
 * @author martinjiang
 * @version 1.0
 * @className SubAccountDepositDto
 * @description 请描述类的业务用途
 * @date 2022/9/8 14:21
 **/
@Data
public class SubAccountDepositDTO {
    /**
     * 子账户名称
     */
    private String subAcct;
    /**
     * 币种名称，如 BTC
     */
    private String ccy;
    /**
     * 区块转账哈希记录
     */
    private String txId;
    /**
     * 充值状态
     * 0：等待确认 1：确认到账 2：充值成功
     */
    private String state;
    /**
     * 查询在此之前的内容，值为时间戳，Unix 时间戳为毫秒数格式，如 1597026383085
     */
    private String after;
    /**
     * 查询在此之后的内容，值为时间戳，Unix 时间戳为毫秒数格式，如 1597026383085
     */
    private String before;
    /**
     * 返回的结果集数量，默认为 100，最大为 100
     */
    private String limit;
}
