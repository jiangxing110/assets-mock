package com.qbit.assets.thirdparty.internal.circle.domain.dto;


import com.qbit.assets.common.enums.ChainType;
import com.qbit.assets.common.enums.CryptoConversionCurrencyEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author litao
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AddressDTO extends BaseDTO {
    private CryptoConversionCurrencyEnum currency;

    private ChainType chain;
}
