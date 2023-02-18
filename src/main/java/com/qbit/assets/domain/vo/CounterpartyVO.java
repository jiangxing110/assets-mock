package com.qbit.assets.domain.vo;

import com.qbit.assets.common.enums.ChainType;
import com.qbit.assets.common.enums.CounterpartyType;
import com.qbit.assets.common.enums.CryptoConversionCurrencyEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author litao
 */
@Data
public class CounterpartyVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 交易方类型
     * chain, global account, wallet
     */
    private CounterpartyType type;

    /**
     * 交易方币种(选填)
     */
    private CryptoConversionCurrencyEnum currency;

    /**
     * type为chain address
     */
    private String address;

    /**
     * 链
     */
    private ChainType chain;

    /**
     * 全球账户nickname
     */
    private String nickname;

    /**
     * 收款人姓名
     */
    private String username;
}
