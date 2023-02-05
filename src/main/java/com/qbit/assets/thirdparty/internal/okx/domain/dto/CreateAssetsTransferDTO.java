package com.qbit.assets.thirdparty.internal.okx.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 资金划转
 *
 * @author litao
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CreateAssetsTransferDTO extends BaseDTO {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 币种，如 USDT
     */
    private String ccy;

    /**
     * 划转数量
     */
    private String amt;

    /**
     * 转出账户
     * 6：资金账户 18：交易账户
     */
    private String from;

    /**
     * 转入账户
     * 6：资金账户 18：交易账户
     */
    private String to;

    /**
     * 子账户名称，type 为1，2 或 4：subAcct 为必填项
     */
    private String subAcct;

    /**
     * 划转类型
     * 0：账户内划转
     * 1：母账户转子账户(仅适用于母账户APIKey)
     * 2：子账户转母账户(仅适用于母账户APIKey)
     * 3：子账户转母账户(仅适用于子账户APIKey)
     * 4：子账户转子账户(仅适用于子账户APIKey，且目标账户需要是同一母账户下的其他子账户)
     * 默认是0
     */
    private String type;

    /**
     * 是否支持跨币种保证金模式或组合保证金模式下的借币转入/转出
     * true 或 false，默认false
     */
    private boolean loanTrans;

    /**
     * 客户自定义ID
     * 字母（区分大小写）与数字的组合，可以是纯字母、纯数字且长度要在1-32位之间。
     */
    private String clientId;

    /**
     * 是否忽略仓位风险
     * 默认为false
     * 仅适用于组合保证金模式
     */
    private String omitPosRisk;

}
