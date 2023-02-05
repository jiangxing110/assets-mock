package com.qbit.assets.thirdparty.internal.okx.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 闪兑币种信息
 *
 * @author litao
 */
@Data
public class AssetsCurrencyVO implements Serializable {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 币种名称，如 BTC
     */
    private String ccy;

    /**
     * 币种中文名称，不显示则无对应名称
     */
    private String name;

    /**
     * 币种Logo链接
     */
    private String logoLink;

    /**
     * 币种链信息
     * 有的币种下有多个链，必须要做区分，如USDT下有USDT-ERC20，USDT-TRC20，USDT-Omni多个链
     */
    private String chain;

    /**
     * 是否可充值，false表示不可链上充值，true表示可以链上充值
     */
    private Boolean canDep;

    /**
     * 是否可提币，false表示不可链上提币，true表示可以链上提币
     */
    private Boolean canWd;

    /**
     * 是否可内部转账，false表示不可内部转账，true表示可以内部转账
     */
    private Boolean canInternal;

    /**
     * 币种单笔最小充值量
     */
    private String minDep;

    /**
     * 币种单笔最小提币量
     */
    private String minWd;

    /**
     * 币种单笔最大提币量
     */
    private String maxWd;

    /**
     * 提币精度,表示小数点后的位数
     */
    private String wdTickSz;

    /**
     * 过去24小时内提币额度，单位为BTC
     */
    private String wdQuota;

    /**
     * 过去24小时内已用提币额度，单位为BTC
     */
    private String usedWdQuota;

    /**
     * 最小提币手续费数量
     */
    private String minFee;

    /**
     * 最大提币手续费数量
     */
    private String maxFee;

    /**
     * 当前链是否为主链
     * 如果是则返回true，否则返回false
     */
    private Boolean mainNet;

    /**
     * 当前链是否需要标签（tag/memo）信息
     */
    private Boolean needTag;

    /**
     * 充值到账最小网络确认数。币已到账但不可提。
     */
    private String minDepArrivalConfirm;

    /**
     * 提现解锁最小网络确认数
     */
    private String minWdUnlockConfirm;

    /**
     * 充币固定限额，单位为BTC
     * 没有充币限制则返回""
     */
    private String depQuotaFixed;

    /**
     * 已用充币固定额度，单位为BTC
     * 没有充币限制则返回""
     */
    private String usedDepQuotaFixed;
}
