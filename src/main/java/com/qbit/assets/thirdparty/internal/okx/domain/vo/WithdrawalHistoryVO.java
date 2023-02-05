package com.qbit.assets.thirdparty.internal.okx.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * WithdrawalHistoryVO
 *
 * @author litao
 * @date 2022/9/14 10:40
 */
@Data
public class WithdrawalHistoryVO implements Serializable {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 币种
     */
    private String ccy;

    /**
     * 币种链信息
     * 有的币种下有多个链，必须要做区分，如USDT下有USDT-ERC20，USDT-TRC20，USDT-Omni多个链
     */
    private String chain;

    /**
     * 数量
     */
    private String amt;

    /**
     * 提币申请时间，Unix 时间戳的毫秒数格式，如 1655251200000
     */
    private String ts;

    /**
     * 提币地址（如果收币地址是 OKX 平台地址，则此处将显示用户账户）
     */
    private String from;

    /**
     * 收币地址
     */
    private String to;

    /**
     * 部分币种提币需要标签，若不需要则不返回此字段
     */
    private String tag;

    /**
     * 部分币种提币需要此字段（payment_id），若不需要则不返回此字段
     */
    private String pmtId;

    /**
     * 部分币种提币需要此字段，若不需要则不返回此字段
     */
    private String memo;

    /**
     * 提币哈希记录
     * 内部转账该字段返回""
     */
    private String txId;

    /**
     * 提币手续费
     */
    private String fee;

    /**
     * 提币状态
     * -3：撤销中 -2：已撤销 -1：失败
     * 0：等待提现 1：提现中 2：已汇出
     * 3：邮箱确认 4：人工审核中 5：等待身份认证
     */
    private String state;

    /**
     * 提币申请ID
     */
    private String wdId;

    /**
     * 客户自定义ID
     */
    private String clientId;
}
