package com.qbit.assets.thirdparty.internal.okx.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 资金账户信息
 *
 * @author litao
 */
@Data
public class AssetsBalanceVO implements Serializable {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 币种，如 BTC
     */
    private String ccy;

    /**
     * 余额
     */
    private String bal;

    /**
     * 冻结（不可用）
     */
    private String frozenBal;

    /**
     * 可用余额
     */
    private String availBal;
}
