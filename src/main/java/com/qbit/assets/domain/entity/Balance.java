package com.qbit.assets.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.common.enums.CryptoConversionCurrencyEnum;
import com.qbit.assets.common.enums.WalletTypeEnum;
import com.qbit.assets.domain.base.BaseV2;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author martin
 * @since 2023-02-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assets_balance")
public class Balance extends BaseV2 {

    private static final long serialVersionUID = 1L;

    private String remarks;

    private String accountId;

    private WalletTypeEnum walletType;

    private CryptoConversionCurrencyEnum currency;

    private BigDecimal available;

    private BigDecimal pending;

    private BigDecimal frozen;

    private String status;


}
