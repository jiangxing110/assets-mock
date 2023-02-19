package com.qbit.assets.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.common.enums.CryptoConversionCurrencyEnum;
import com.qbit.assets.common.enums.TransactionDisplayStatusEnum;
import com.qbit.assets.common.enums.TransactionStatusEnum;
import com.qbit.assets.common.enums.TransactionTypeEnum;
import com.qbit.assets.domain.base.BaseV2;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assets_transaction")
public class Transaction extends BaseV2 {

    private static final long serialVersionUID = 1L;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 所属账户id
     */
    private String accountId;

    /**
     * 源交易账号来源((Wallet, PayPal, 银行卡，银行账户的id))
     */
    private String sender;

    /**
     * 源交易类型(Wallet, PayPal, 银行卡，银行账户)
     */
    private String senderType;

    /**
     * 源的余额id
     */
    private String senderBalanceId;

    /**
     * 目标交易(Wallet, PayPal, 银行卡，银行账户的id)
     */
    private String recipient;

    /**
     * 目标交易类型
     */
    private String recipientType;

    /**
     * 目标的余额id
     */
    private String recipientBalanceId;
    /**
     * 源币种
     */
    private CryptoConversionCurrencyEnum senderCurrency;

    /**
     * 目标币种
     */
    private CryptoConversionCurrencyEnum recipientCurrency;

    /**
     * 源费用
     */
    private BigDecimal senderFee;

    /**
     * 源费用类型
     */
    private String senderFeeType;

    /**
     * 目标费用
     */
    private BigDecimal recipientFee;

    /**
     * 目标费用类型
     */
    private String recipientFeeType;

    /**
     * 源金额
     */
    private BigDecimal senderCost;

    /**
     * 目标金额
     */
    private BigDecimal recipientCost;

    /**
     * 状态
     */
    private TransactionStatusEnum status;

    /**
     * 状态
     */
    private TransactionDisplayStatusEnum displayStatus;

    private String statusLog;

    /**
     * 交易时间，如：三方的交易时间，我方的创建时间
     */
    private Date transactionTime;

    /**
     * 钱包操作类型
     */
    private String balanceOperationType;

    /**
     * 统一订单号，全局唯一，用于展示
     */
    private String transactionDisplayId;

    /**
     * 源订单id
     */
    private String sourceId;

    /**
     * 源订单类型，如 penny
     */
    private String sourceType;

    /**
     * 源订单二级类型，如 pay
     */
    private TransactionTypeEnum type;

    /**
     * api 开放给三方的时的备注id
     */
    private String apiTransactionId;


}
