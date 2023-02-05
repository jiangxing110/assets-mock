package com.qbit.assets.common.enums;


import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author litao
 */

@Getter
public enum TransactionStatusEnum implements IEnum<String> {
    /**
     * 处理中
     */
    Pending("Pending"),
    /**
     * 交易正常结束
     */
    Closed("Closed"),
    /**
     * 失败
     */
    Fail("Fail"),

    /**
     * 待用户补录
     */
    ToBeSupplementedByUser("ToBeSupplementedByUser");

    @JsonValue
    public final String value;

    TransactionStatusEnum(String value) {
        this.value = value;
    }

    public static TransactionStatusEnum getItem(String token) {
        for (TransactionStatusEnum item : values()) {
            if (item.value.equals(token)) {
                return item;
            }
        }
        return null;
    }

    /**
     * status 与 displayStatus 状态映射
     *
     * @param status transfer status
     * @return transaction status
     */
    public static TransactionStatusEnum formStatus(CryptoAssetsTransferStatus status) {
        return switch (status) {
            case Closed -> Closed;
            case Rejected, Cancelled -> Fail;
            case ToBeSupplementedByUser -> ToBeSupplementedByUser;
            default -> Pending;
        };
    }
}
