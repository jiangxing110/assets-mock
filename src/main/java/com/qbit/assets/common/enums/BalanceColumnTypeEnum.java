package com.qbit.assets.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 数据库列enum(不要修改成大写)
 *
 * @author martinjiang
 */
public enum BalanceColumnTypeEnum {
    Available("available"),
    Pending("Pending"),
    Frozen("frozen");

    @JsonValue
    public final String value;

    private BalanceColumnTypeEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
