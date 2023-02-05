package com.qbit.assets.thirdparty.internal.okx.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * WithdrawalVO
 *
 * @author litao
 * @date 2022/9/9 12:49
 */
@Data
public class WithdrawalVO implements Serializable {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 提币币种
     */
    private String ccy;

    /**
     * 币种链信息
     * 有的币种下有多个链，必须要做区分，如USDT下有USDT-ERC20，USDT-TRC20，USDT-Omni多个链
     */
    private String chain;

    /**
     * 提币数量
     */
    private String amt;

    /**
     * 提币申请ID
     */
    private String wdId;

    /**
     * 客户自定义ID
     */
    private String clientId;
}
