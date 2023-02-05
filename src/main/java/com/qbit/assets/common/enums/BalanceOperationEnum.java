package com.qbit.assets.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author martinjiang
 */

public enum BalanceOperationEnum {
    Add("Add"), Sub("Sub");

    @JsonValue
    public final String value;

    private BalanceOperationEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
