package com.qbit.assets.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.common.enums.*;
import com.qbit.assets.domain.base.BaseV2;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author martinjiang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assets_transfer")
public class CryptoAssetsTransfer extends BaseV2 {

    private static final long serialVersionUID = 1L;

    /**
     * 关联的balance id
     */
    private String balanceId;

    /**
     * 交易状态
     */
    private CryptoAssetsTransferStatus status;

    /**
     * 交易类型(in/out)(转入/转出)
     */
    private CryptoAssetsTransferAction action;

    /**
     * 对方加密货币地址
     */
    private String address;

    /**
     * 交易金额
     */
    private BigDecimal settlementAmount;

    /**
     * 交易手续费
     */
    private BigDecimal fee;

    /**
     * 关联的三方交易id
     */
    private String tradeId;

    /**
     * 链
     */
    private ChainType chain;

    /**
     * 结算币种
     */
    private CryptoConversionCurrencyEnum currency;

    private String transactionId;

    /**
     * 交易时间
     */
    private Date transactionTime;

    /**
     * 关联账号
     */
    private String accountId;

    private BigDecimal originAmount;

    /**
     * 汇率
     */
    private BigDecimal rate;

    /**
     * 交易状态
     */
    private TransactionStatusEnum displayStatus;

    /**
     * 交易发起方币种
     */
    private CryptoConversionCurrencyEnum quoteCurrency;

    /**
     * 发送方类型
     */
    private CounterpartyType senderType;

    /**
     * 接收方类型
     */
    private CounterpartyType recipientType;

    /**
     * 收款人id
     */
    private String payeeId;

    /**
     * 拒绝原因
     */
    private String reason;


    private String rawData;

    /**
     * 出入金账户关系
     */
    private String accountRelation;

    private String inboundType;

    /**
     * 退款来源的交易ID(transaction display id)
     */
    private String relatedQbitTxId;

    /**
     * 对商户端隐藏(admin可以查看)
     */
    private Boolean hidden;


}
