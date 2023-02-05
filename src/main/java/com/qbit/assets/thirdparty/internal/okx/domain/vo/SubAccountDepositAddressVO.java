package com.qbit.assets.thirdparty.internal.okx.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 子账户充值地址信息
 *
 * @author litao
 */
@Data
public class SubAccountDepositAddressVO implements Serializable {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 链
     */
    private String chain;

    /**
     * 币种
     */
    private String ccy;

    /**
     * 充值地址
     */
    private String addr;

    /**
     * 部分币种提币需要此字段，如果不需要此字段的币种吗，返回”“ ，如 xmr
     */
    private String pmtId;

    /**
     * 部分币种提币需要此字段，如果不需要此字段的币种吗，返回”“ ， 如XRP
     */
    private String tag;

    /**
     * 部分币种充值需要标签，若不需要则不返回此字段，返回”“，如eos
     */
    private String memo;


    /**
     * 创建时间，Unix 时间戳的毫秒数格式，如 1597026383085
     */
    private String ts;
}
