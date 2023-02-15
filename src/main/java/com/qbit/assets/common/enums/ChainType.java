package com.qbit.assets.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author litao
 */

@Getter
public enum ChainType implements IEnum<String> {
    /**
     * BTC
     */
    BTC("BTC", "Bitcoin"),
    /**
     * ETH
     */
    ETH("ETH", "Ethereum"),
    /**
     * OKT
     */
    OKT("OKT", "OKC"),
    /**
     * MATIC
     */
    MATIC("MATIC", "Polygon"),
    /**
     * ALGO
     */
    ALGO("ALGO", "Algorand"),
    /**
     * AVAX
     */
    AVAX("AVAX", "Avalanche"),
    /**
     * FLOW
     */
    FLOW("FLOW", "Flow"),
    /**
     * HBAR
     */
    HBAR("HBAR", "Hedera Hashgraph"),
    /**
     * SOL
     * tips: circle测试环境创建不出来了
     */
    SOL("SOL", "Solana"),
    /**
     * TRX
     */
    TRX("TRX", "Tron"),
    /**
     * XLM
     */
    XLM("XLM", "Stellar"),
    /**
     * NA
     */
    NA("NA", "Na");

    /**
     * 代币名称
     */
    @JsonValue
    private final String token;

    /**
     * 主链全称
     */
    private final String name;

    ChainType(String token, String name) {
        this.token = token;
        this.name = name;
    }

    public static ChainType getItem(String token) {
        for (ChainType item : values()) {
            if (item.token.equals(token)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public String getValue() {
        return token;
    }
}
