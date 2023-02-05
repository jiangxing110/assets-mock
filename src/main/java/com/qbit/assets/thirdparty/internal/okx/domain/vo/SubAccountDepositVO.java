package com.qbit.assets.thirdparty.internal.okx.domain.vo;

import lombok.Data;

/**
 * @author martinjiang
 * @version 1.0
 * @className SubAccountDepositVo
 * @description 请描述类的业务用途
 * @date 2022/9/8 14:10
 **/
@Data
public class SubAccountDepositVO {
    /**
     * 充值数量
     */
    private String amt;
    /**
     * 最新的充币网络确认数
     */
    private String actualDepBlkConfirm;
    /**
     * 区块转账哈希记录
     */
    private String txId;
    /**
     * 币种名称，如 BTC
     */
    private String ccy;
    /**
     * 币种链信息
     * 有的币种下有多个链，必须要做区分，如USDT下有USDT-ERC20，USDT-TRC20，USDT-Omni多个链
     */
    private String chain;
    /**
     * 充值地址，只显示内部账户转账地址，不显示区块链充值地址
     */
    private String from;
    /**
     * 到账地址
     */
    private String to;
    /**
     * 充值到账时间，Unix 时间戳的毫秒数格式，如 1597026383085
     */
    private String ts;
    /**
     * 充值状态
     * 0：等待确认
     * 1：确认到账
     * 2：充值成功
     * 8：因该币种暂停充值而未到账，恢复充值后自动到账
     * 12：账户或充值被冻结
     * 13：子账户充值拦截
     */
    private String state;
    /**
     * 子账户名称
     */
    private String subAcct;
    /**
     * 充值记录 ID
     */
    private String depId;
}
