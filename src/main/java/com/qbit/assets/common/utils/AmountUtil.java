package com.qbit.assets.common.utils;


import com.qbit.assets.common.enums.CryptoConversionCurrencyEnum;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author litao
 */
public class AmountUtil {
    /**
     * 内部做信息流的币种
     */
    public static final List<CryptoConversionCurrencyEnum> INTERNAL_STREAM_COINS = Arrays.asList(
            CryptoConversionCurrencyEnum.USD,
            CryptoConversionCurrencyEnum.USDC
    );

    /**
     * 稳定币
     */
    public static final List<CryptoConversionCurrencyEnum> STABLE_COINS = Arrays.asList(
            CryptoConversionCurrencyEnum.USD,
            CryptoConversionCurrencyEnum.USDC,
            CryptoConversionCurrencyEnum.USDT
    );

    /**
     * 验证金额是否在固定的精度内
     *
     * @param amount    金额
     * @param precision 精度
     * @return 验证结果
     */
    public static boolean verifyDecimal(double amount, int precision) {
        String regex = "^\\d+(\\.\\d{1," + precision + "})?$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(BigDecimal.valueOf(amount).toPlainString()).matches();
    }

    /**
     * 获取币种精度
     *
     * @param currency 币种
     * @return precision
     */
    public static int getPrecision(CryptoConversionCurrencyEnum currency) {
        // circle平台USDC不允许超出2位小数点
        return switch (currency) {
            case USD, USDC, USDT -> 2;
            default -> 8;
        };
    }

    /**
     * 获取汇率精度
     *
     * @param baseCurrency  交易币
     * @param quoteCurrency 计价币
     * @param rfqCurrency   询价币
     * @return precision
     */
    public static int getPrecision(CryptoConversionCurrencyEnum baseCurrency, CryptoConversionCurrencyEnum quoteCurrency, CryptoConversionCurrencyEnum rfqCurrency) {
        if (INTERNAL_STREAM_COINS.contains(baseCurrency) && INTERNAL_STREAM_COINS.contains(quoteCurrency)) {
            return 2;
        }
        if (rfqCurrency == baseCurrency && baseCurrency == CryptoConversionCurrencyEnum.BTC && STABLE_COINS.contains(quoteCurrency)) {
            return 8;
        }
        return 6;
    }

    /**
     * 验证金额和对应的币种精度是否匹配
     *
     * @param currency 币种
     * @param amount   金额
     * @return 验证结果
     */
    public static boolean verify(CryptoConversionCurrencyEnum currency, double amount) {
        int precision = getPrecision(currency);
        return verifyDecimal(amount, precision);
    }

    /**
     * 是否是稳定币
     *
     * @param currency 币种
     * @return ret
     */
    public static boolean isStableCoin(CryptoConversionCurrencyEnum currency) {
        return STABLE_COINS.contains(currency);
    }
}
