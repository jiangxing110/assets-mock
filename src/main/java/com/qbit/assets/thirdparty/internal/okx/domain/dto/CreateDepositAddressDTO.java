package com.qbit.assets.thirdparty.internal.okx.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 创建子账户充值地址
 *
 * @author litao
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CreateDepositAddressDTO extends BaseDTO {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 子账户名称
     */
    private String subAcct;

    /**
     * 币种名称，仅支持大写，如 BTC
     */
    private String ccy;

    /**
     * 币种链信息
     * 有的币种下有多个链，必须要做区分，如USDT下有USDT-ERC20，USDT-TRC20，USDT-Omni多个链
     * 如果不填此参数，则默认为主链
     */
    private String chain;

    /**
     * 充值地址类型
     * 1:普通地址
     * 2:隔离验证地址
     * 默认为普通地址，仅适用于btc和ltc
     */
    private String addrType;

    /**
     * 充值到账账户
     * 6：资金账户 18：交易账户
     * 默认是6
     */
    private String to;
}
