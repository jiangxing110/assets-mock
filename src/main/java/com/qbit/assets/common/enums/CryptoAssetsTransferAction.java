package com.qbit.assets.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author litao
 */

@Getter
public enum CryptoAssetsTransferAction implements IEnum<String> {
    /**
     * 充值
     */
    IN("in", "充值"),
    /**
     * 转出
     */
    OUT("out", "转出"),
    /**
     * 退款
     */
    REFUND("refund", "退款"),
    /**
     * 买入
     */
    BUY("buy", "买入"),
    /**
     * 卖出
     */
    SELL("sell", "卖出"),
    /**
     * 收益 加密资产对账流水导出
     */
    PROFIT("profit", "收益");

    @JsonValue
    private final String value;

    private final String name;

    CryptoAssetsTransferAction(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static CryptoAssetsTransferAction getItem(String value) {
        for (CryptoAssetsTransferAction item : values()) {
            if (item.value.equals(value)) {
                return item;
            }
        }
        return null;
    }
}
