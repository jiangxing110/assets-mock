package com.qbit.assets.thirdparty.internal.circle.enums;


import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author litao
 */

@Getter
public enum CircleWalletTypeEnum implements IEnum<String> {
    /**
     * 卡
     */
    CARD("card"),
    /**
     * 钱包
     */
    WALLET("wallet"),
    /**
     * 区块链
     */
    BLOCKCHAIN("blockchain"),
    /**
     * bank wire
     */
    WIRE("wire"),

    /**
     * signet bank
     * tips: qbit暂未支持该类型
     */
    SIGNET("signet"),

    /**
     * Silvergate SEN bank
     * tips: qbit暂未支持该类型
     */
    SEN("sen");

    @JsonValue
    private final String value;

    CircleWalletTypeEnum(String value) {
        this.value = value;
    }

    public static CircleWalletTypeEnum getItem(String value) {
        for (CircleWalletTypeEnum item : values()) {
            if (item.value.equals(value)) {
                return item;
            }
        }
        return null;
    }

}
