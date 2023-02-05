package com.qbit.assets.thirdparty.internal.okx.domain.vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 闪兑币种信息
 *
 * @author litao
 */
@Data
public class ConvertCurrencyVO implements Serializable {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 币种名称，如 BTC
     */
    @JsonAlias("ccy")
    private String currency;

    /**
     * 支持闪兑的最小值
     */
    private String min;

    /**
     * 支持闪兑的最大值
     */
    private String max;
}
