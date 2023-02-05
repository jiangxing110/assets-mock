package com.qbit.assets.thirdparty.internal.circle.domain.bo;


import com.qbit.assets.common.enums.CryptoConversionCurrencyEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author litao
 */
@Data
public class AmountBO implements Serializable {
    private String amount;

    private CryptoConversionCurrencyEnum currency;
}
