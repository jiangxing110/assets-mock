package com.qbit.assets.thirdparty.internal.circle.enums;


import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author LiTao litaoh@aliyun.com
 */
@Getter
public enum CircleCardStatus implements IEnum<String> {
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

    @JsonValue
    private final String value;

    CircleCardStatus(String value) {
        this.value = value;
    }
}
