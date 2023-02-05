package com.qbit.assets.thirdparty.internal.circle.enums;


import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author litao
 */

@Getter
public enum CircleNotificationTypeEnum implements IEnum<String> {

    /**
     * 交易
     */
    TRANSFERS("transfers"),

    /**
     * payouts
     */
    PAYOUTS("payouts"),

    /**
     * 退款
     */
    RETURNS("returns"),

    /**
     * wire
     */
    WIRE("wire"),

    /**
     * cards
     */
    CARDS("cards"),

    /**
     * payments
     */
    PAYMENTS("payments");

    @JsonValue
    private final String value;

    CircleNotificationTypeEnum(String value) {
        this.value = value;
    }

    public static CircleNotificationTypeEnum getItem(String value) {
        for (CircleNotificationTypeEnum item : values()) {
            if (item.value.equals(value)) {
                return item;
            }
        }
        return null;
    }

}
