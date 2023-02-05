package com.qbit.assets.thirdparty.internal.circle.enums;


import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author LiTao litaoh@aliyun.com
 */
@Getter
public enum CircleVerificationType implements IEnum<String> {
    /**
     * none
     */
    NONE("none");

    @JsonValue
    private final String value;

    CircleVerificationType(String value) {
        this.value = value;
    }

}
