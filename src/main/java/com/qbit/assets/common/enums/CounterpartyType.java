package com.qbit.assets.common.enums;


import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 交易对手方
 *
 * @author litao
 */
@Getter
public enum CounterpartyType implements IEnum<String> {
    /**
     * 链
     */
    CHAIN("chain", "外部钱包"),

    /**
     * 钱包
     */
    WALLET("wallet", "钱包"),

    /**
     * 外部银行(手动出金)
     */
    OUTSIDE_BANK("outside_bank", "外部银行"),

    /**
     * 外部银行账户
     */
    WIRE("wire", "外部账户");

    @JsonValue
    private final String value;

    private final String name;

    CounterpartyType(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static CounterpartyType getItem(String value) {
        for (CounterpartyType item : values()) {
            if (item.value.equals(value)) {
                return item;
            }
        }
        return null;
    }
}
