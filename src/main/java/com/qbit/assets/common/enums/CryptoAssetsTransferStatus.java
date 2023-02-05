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
     * 未知
     */
    Na("Na"),
    /**
     * 待判断
     */
    ToBeJudged("ToBeJudged"),
    /**
     * 待合规补录
     */
    ToBeSupplementedByCompliance("ToBeSupplementedByCompliance"),
    /**
     * 待用户补录
     */
    ToBeSupplementedByUser("ToBeSupplementedByUser"),
    /**
     * 待复审
     */
    Reexamine("Reexamine"),
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


}
