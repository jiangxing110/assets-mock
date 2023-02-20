package com.qbit.assets.common.utils;


import com.qbit.assets.common.enums.ChainType;

import java.util.Arrays;
import java.util.List;

/**
 * Okx工具类
 *
 * @author litao
 * @date 2022/9/13 11:43
 */
public class OkxUtil {
    /**
     * qbit链信息转换成okx链信息
     *
     * @param chain 链
     * @return okx链信息
     */
    public static String convertChain(ChainType chain) {
        return switch (chain) {
            case OKT -> "OKC";
            case BTC -> "Bitcoin";
            case ETH -> "ERC20";
            case TRX -> "TRC20";
            case MATIC -> "Polygon";
            case SOL -> "Solana";
            case AVAX -> "Avalanche C-Chain";
            default -> throw new IllegalStateException("Unexpected value: " + chain);
        };
    }

    public static ChainType nameToChain(String fullName) {
        List<String> list = Arrays.asList(fullName.split("-"));
        if (list.size() > 1) {
            list = list.subList(1, list.size());
        }
        return convertChain(String.join("-", list));
    }

    /**
     * okx链信息转换成qbit链信息
     *
     * @param chain okx链信息
     * @return {@link ChainType}
     * @author martinjiang
     * @date 2022/9/13 17:21
     */
    public static ChainType convertChain(String chain) {
        return switch (chain) {
            case "OKC" -> ChainType.OKT;
            case "Bitcoin" -> ChainType.BTC;
            case "ERC20" -> ChainType.ETH;
            case "TRC20" -> ChainType.TRX;
            case "Polygon" -> ChainType.MATIC;
            case "Solana" -> ChainType.SOL;
            case "Avalanche C-Chain" -> ChainType.AVAX;
            default -> null;
        };
    }

}
