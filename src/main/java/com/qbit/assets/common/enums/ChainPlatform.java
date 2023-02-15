package com.qbit.assets.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author martinjiang
 * @description ChainPlatform
 * @date 2022/9/13 15:07
 */
@Getter
public enum ChainPlatform implements IEnum<String> {
    /**
     * BTC
     */
    BTC("Bitcoin", "BTC"),
    /**
     * ETH
     */
    ETH("Ethereum", "ETH"),
    USDTERC20("USDT-ERC20", "USDT"),
    USDTTRC20("USDT-TRC20", "USDT"),
    USDTPolygon("USDT-Polygon", "USDT"),
    USDTTron("USDT-Avalanche C-Chain", "USDT");


    /**
     * 代币名称
     */
    @JsonValue
    private final String token;

    /**
     * 主链全称
     */
    private final String name;

    ChainPlatform(String token, String name) {
        this.token = token;
        this.name = name;
    }

    public static ChainPlatform getItem(String name) {
        for (ChainPlatform item : values()) {
            if (item.name.equals(name)) {
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
