package com.qbit.assets.thirdparty.internal.circle.enums;


import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author LiTao litaoh@aliyun.com
 */
@Getter
public enum CirclePaymentStatus implements IEnum<String> {
    /**
     * 完成
     */
    CONFIRMED("confirmed"),
    /**
     * 处理中
     */
    PENDING("pending"),
    /**
     * 失败
     */
    FAILED("failed"),
    /**
     * 已支付
     */
    PAID("paid"),
    /**
     * 待办
     */
    ACTION_REQUIRED("action_required");

    @JsonValue
    private final String value;

    CirclePaymentStatus(String value) {
        this.value = value;
    }
}
