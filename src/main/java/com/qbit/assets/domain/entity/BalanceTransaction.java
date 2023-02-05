package com.qbit.assets.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.common.enums.CryptoConversionCurrencyEnum;
import com.qbit.assets.common.enums.TransactionStatusEnum;
import com.qbit.assets.common.utils.JsonbTypeHandler;
import com.qbit.assets.domain.base.BaseV2;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assets_balance_transaction")
public class BalanceTransaction extends BaseV2 {

    private static final long serialVersionUID = 1L;

    @TableField(value = "\"accountId\"")
    private String accountId;

    @TableField(value = "\"sourceType\"")
    private String sourceType;

    @TableField(value = "\"sourceId\"")
    private String sourceId;

    private TransactionStatusEnum type;

    private String sender;

    private String senderBalanceId;

    private String senderType;

    private CryptoConversionCurrencyEnum senderCurrency;

    private BigDecimal senderFee;

    private String senderFeeType;

    private BigDecimal senderCost;

    private String recipient;

    private Object recipientBalanceId;

    private String recipientType;

    private CryptoConversionCurrencyEnum recipientCurrency;

    @TableField(value = "\"recipientFee\"")
    private BigDecimal recipientFee;

    private String recipientFeeType;

    private BigDecimal recipientCost;

    private BigDecimal senderCurrentAvailableAmount;

    private BigDecimal senderCurrentPendingAmount;

    private BigDecimal senderCurrentFrozenAmount;

    private BigDecimal recipientCurrentAvailableAmount;

    private BigDecimal recipientCurrentPendingAmount;

    private BigDecimal recipientCurrentFrozenAmount;

    @TableField(select = false, typeHandler = JsonbTypeHandler.class)
    private Object sqlExecuteList;
    @TableField(value = "remarks")
    private String remarks;
}
