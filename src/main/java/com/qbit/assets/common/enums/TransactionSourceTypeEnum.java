package com.qbit.assets.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author martinjiang
 */

public enum TransactionSourceTypeEnum {
    /**
     * 量子卡
     */
    QbitCard("QbitCard"),
    /**
     * 全球账户转账
     */
    GlobalAccountTransfer("GlobalAccountTransfer"),
    /**
     * 子母账户转账
     */
    QbitCardWalletInternalTransfer("QbitCardWalletInternalTransfer"),
    /**
     * 加密货币
     */
    QbitCrypto("QbitCrypto"),
    /**
     * 粒子理财
     */
    Financing("Financing"),
    /**
     * 加密货币
     */
    Crypto("Crypto"),
    /**
     * 收单工具
     */
    Payment("Payment"),
    /**
     * 冻结
     */
    Frozen("Frozen"),
    /**
     * 解冻
     */
    Unfrozen("Unfrozen");

    @JsonValue
    public final String value;

    private TransactionSourceTypeEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
