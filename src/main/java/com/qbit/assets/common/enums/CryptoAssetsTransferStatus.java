package com.qbit.assets.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author litao
 */

@Getter
public enum CryptoAssetsTransferStatus implements IEnum<String> {

    /**
     * 等待合规审核
     */
    Pending("Pending"),
    /**
     * 处理中
     */
    Processing("Processing"),
    /**
     * 正常结束
     */
    Closed("Closed"),
    /**
     * 取消
     */
    Cancelled("Cancelled"),
    /**
     * 拒绝(三方合规拒绝)
     */
    Rejected("Rejected");


    @JsonValue
    private final String value;

    CryptoAssetsTransferStatus(String value) {
        this.value = value;
    }

    public static CryptoAssetsTransferStatus getItem(String value) {
        for (CryptoAssetsTransferStatus item : values()) {
            if (item.value.equals(value)) {
                return item;
            }
        }
        return null;
    }

    public static CryptoAssetsTransferStatus valueOf(TransactionStatusEnum status) {
        switch (status) {
            case Closed -> {
                return Closed;
            }
            case Pending -> {
                return Processing;
            }
            default -> {
                return Cancelled;
            }
        }
    }
}
