package com.qbit.assets.domain.dto;


import com.qbit.assets.common.enums.CryptoConversionCurrencyEnum;
import com.qbit.assets.common.enums.TransactionTypeEnum;
import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * 充值订单创建 参数
 *
 * @author EDZ
 */
@Data
public class CreateCryptoRechargeDTO implements Serializable {
    private String transferId;

    private CryptoConversionCurrencyEnum walletCurrency;
    private String counterParty;

    private String source;

    @Min(0)
    private Double originAmount;

    @Min(0)
    private Double settlementAmount;

    @Min(0)
    private Double rate;

    private CryptoConversionCurrencyEnum currency;


    private TransactionTypeEnum type;

    private CryptoConversionCurrencyEnum quoteCurrency;

}
