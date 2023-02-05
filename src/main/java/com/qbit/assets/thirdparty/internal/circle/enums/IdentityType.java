package com.qbit.assets.thirdparty.internal.circle.enums;


import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author litao
 */

@Getter
public enum IdentityType implements IEnum<String> {
    /**
     * 个人
     */
    INDIVIDUAL("individual"),

    /**
     * 企业
     */
    BUSINESS("business");

    @JsonValue
    private final String value;

    IdentityType(String value) {
        this.value = value;
    }
}
