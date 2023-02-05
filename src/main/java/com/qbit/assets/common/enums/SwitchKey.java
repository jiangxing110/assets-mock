package com.qbit.assets.common.enums;


import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author martinjiang
 * @description RedisKey
 * @date 2023/2/5 10:07
 */
@Getter
public enum SwitchKey implements IEnum<String> {

    ADDRESS("ADDRESS", "地址开关");


    @JsonValue
    private final String key;

    private final String value;

    SwitchKey(String value, String key) {
        this.value = value;
        this.key = key;
    }

    public static SwitchKey getItem(String key) {
        for (SwitchKey item : values()) {
            if (item.key.equals(key)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public String getValue() {
        return value;
    }
}
