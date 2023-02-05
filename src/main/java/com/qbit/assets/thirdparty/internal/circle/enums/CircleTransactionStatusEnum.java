package com.qbit.assets.thirdparty.internal.circle.enums;


import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author litao
 */

public enum CircleTransactionStatusEnum implements IEnum<String> {
    /**
     * 完成
     */
    COMPLETE("complete"),
    /**
     * 处理中
     */
    PENDING("pending"),
    /**
     * 失败
     */
    FAILED("failed");

    private final String value;

    CircleTransactionStatusEnum(String value) {
        this.value = value;
    }

    public static CircleTransactionStatusEnum getItem(String value) {
        for (CircleTransactionStatusEnum item : values()) {
            if (item.value.equals(value)) {
                return item;
            }
        }
        return null;
    }

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
