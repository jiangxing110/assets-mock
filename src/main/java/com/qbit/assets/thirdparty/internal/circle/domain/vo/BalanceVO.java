package com.qbit.assets.thirdparty.internal.circle.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author litao
 */
@Data
public class BalanceVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 币种
     */
    private String currency;

    /**
     * 余额
     */
    private String amount;
}
