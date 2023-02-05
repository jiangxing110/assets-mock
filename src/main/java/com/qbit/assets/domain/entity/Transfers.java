package com.qbit.assets.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.domain.base.BaseV2;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assets_transfers")
public class Transfers extends BaseV2 {

    private static final long serialVersionUID = 1L;

    /**
     * 关联的balance id
     */
    private String balanceId;

    /**
     * 交易状态
     */
    private String status;

    /**
     * 交易类型(in/out)(转入/转出)
     */
    private String action;

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
    private String chain;

    /**
     * 结算币种
     */
    private String currency;

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
    private String displayStatus;

    /**
     * 交易发起方币种
     */
    private String quoteCurrency;

    private String businessTypeDetail;

    /**
     * 发送方类型
     */
    private String senderType;

    /**
     * 接收方类型
     */
    private String recipientType;

    /**
     * 收款人id
     */
    private String payeeId;

    /**
     * 拒绝原因
     */
    private String reason;

    /**
     * 最新处理人
     */
    private String handleUserId;

    /**
     * 对手方
     */
    private String counterParty;

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
     * 交易手续费加点
     */
    private BigDecimal fee2;

    private Date closeTime;

    /**
     * 对商户端隐藏(admin可以查看)
     */
    private Boolean hidden;


}
