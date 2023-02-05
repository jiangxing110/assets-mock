package com.qbit.assets.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author martinjiang
 */

public enum BalanceOperationTypeEnum {
    SingleBalanceAdd("SingleBalanceAdd"),
    SingleBalanceSub("SingleBalanceSub"),
    SystemInternalTransfer("SystemInternalTransfer"),
    Na("Na");

    @JsonValue
    public final String value;

    private BalanceOperationTypeEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
