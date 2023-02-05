package com.qbit.assets.thirdparty.internal.circle.domain.vo;


import com.qbit.assets.common.enums.ChainType;
import com.qbit.assets.common.enums.CryptoConversionCurrencyEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author litao
 */
@Data
public class AddressVO implements Serializable {
    private String address;

    private CryptoConversionCurrencyEnum currency;

    private ChainType chain;

    private String addressTag;

    private Boolean selected;
}
