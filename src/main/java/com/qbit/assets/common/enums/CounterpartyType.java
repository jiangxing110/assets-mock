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
     * 全球账户
     */
    MULTI_CURRENCY_ACCOUNT("multi_currency_account", "美元账户"),
    /**
     * 钱包
     */
    WALLET("wallet", "钱包"),

    /**
     * 量子账户
     */
    VIRTUAL_CARD("virtual_card", "量子账户"),

    /**
     * 外部银行(手动出金)
     */
    OUTSIDE_BANK("outside_bank", "外部银行"),

    /**
     * 粒子理财账户
     */
    FINANCING("financing", "粒子理财本金账户"),

    /**
     * 粒子理财收益账户
     */
    FINANCING_PROFIT("financing_profit", "粒子理财收益账户"),

    /**
     * 外部银行账户
     */
    WIRE("wire", "外部账户"),

    /**
     * 其它
     */
    OTHER("other", "其它"),

    SYSTEM("system", "系统");

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
