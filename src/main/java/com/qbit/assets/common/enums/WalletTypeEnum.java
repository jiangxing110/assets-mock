package com.qbit.assets.common.enums;


import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author martinjiang
 */
@Getter
public enum WalletTypeEnum implements IEnum<String> {
    /**
     * 收单工具
     */
    PaymentWallet("PaymentWallet"),
    /**
     * 虚拟钱包类型
     * 避免以后会出现别的虚拟钱包类型
     */
    VirtualUSD("VirtualUSD"),
    /**
     * Circle的钱包--USDC
     */
    CircleWallet("CircleWallet"),
    /**
     * OKX的钱包
     */
    OkxWallet("OkxWallet"),

    /**
     * Qbit主账户钱包
     */
    QbitWallet("QbitWallet");

    @JsonValue
    public final String value;

    WalletTypeEnum(String value) {
        this.value = value;
    }

    public static WalletTypeEnum getItem(String value) {
        for (WalletTypeEnum item : values()) {
            if (item.value.equals(value)) {
                return item;
            }
        }
        return null;
    }
}
