package com.qbit.assets.common.enums;


import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author martinjiang
 */

@Getter
public enum CryptoConversionCurrencyEnum implements IEnum<String> {

    /**
     * USD
     */
    USD("USD"),

    /**
     * CNY
     */
    CNY("CNY"),

    /**
     * EUR
     */
    EUR("EUR"),

    /**
     * GBP
     */
    GBP("GBP"),

    /**
     * AUD
     */
    AUD("AUD"),

    /**
     * CAD
     */
    CAD("CAD"),

    /**
     * CZK
     */
    CZK("CZK"),

    /**
     * DKK
     */
    DKK("DKK"),

    /**
     * HKD
     */
    HKD("HKD"),

    /**
     * HRK
     */
    HRK("HRK"),

    /**
     * HUF
     */
    HUF("HUF"),

    /**
     * IDR
     */
    IDR("IDR"),

    /**
     * INR
     */
    INR("INR"),

    /**
     * MXN
     */
    MXN("MXN"),

    /**
     * MYR
     */
    MYR("MYR"),

    /**
     * NOK
     */
    NOK("NOK"),

    /**
     * PHP
     */
    PHP("PHP"),

    /**
     * PLN
     */
    PLN("PLN"),

    /**
     * SEK
     */
    SEK("SEK"),

    /**
     * SGD
     */
    SGD("SGD"),

    /**
     * RON
     */
    RON("RON"),

    /**
     * ZAR
     */
    ZAR("ZAR"),

    /**
     * AED
     */
    AED("AED"),

    /**
     * CKZ
     */
    CKZ("CKZ"),

    /**
     * JPY
     */
    JPY("JPY"),

    /**
     * THB
     */
    THB("THB"),

    /**
     * VND
     */
    VND("VND"),

    /**
     * KES
     */
    KES("KES"),

    /**
     * NGN
     */
    NGN("NGN"),

    /**
     * 加密货币
     * USDC
     */
    USDC("USDC"),

    /**
     * USDT
     */
    USDT("USDT"),

    /**
     * BTC
     */
    BTC("BTC"),

    /**
     * ETH
     */
    ETH("ETH");

    @JsonValue
    private final String value;

    CryptoConversionCurrencyEnum(String value) {
        this.value = value;
    }

    public static CryptoConversionCurrencyEnum getItem(String value) {
        for (CryptoConversionCurrencyEnum item : values()) {
            if (item.value.equals(value)) {
                return item;
            }
        }
        return null;
    }
}
