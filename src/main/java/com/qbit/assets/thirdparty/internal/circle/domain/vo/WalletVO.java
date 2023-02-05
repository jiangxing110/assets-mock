package com.qbit.assets.thirdparty.internal.circle.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author litao
 */
@Data
public class WalletVO implements Serializable {
    private String walletId;

    private String entityId;

    private String type;

    private String description;

    private BalanceVO[] balances;
}
