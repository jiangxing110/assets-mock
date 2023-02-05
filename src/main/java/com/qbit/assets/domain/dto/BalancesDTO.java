package com.qbit.assets.domain.dto;

import com.qbit.assets.common.enums.CryptoConversionCurrencyEnum;
import com.qbit.assets.domain.base.QueryBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author litao
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class BalancesDTO extends QueryBase implements Serializable {
    /**
     * balance id
     */
    private String balanceId;

    /**
     * 币种
     */
    private CryptoConversionCurrencyEnum currency;
}
