package com.qbit.assets.common.enums;


import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author EDZ
 * @description TransactionDisplayStatusEnum
 * @date 2022/11/2 11:01
 */
@Getter
public enum TransactionDisplayStatusEnum implements IEnum<String> {
    /**
     * 等待XX
     */
    Pending("Pending"),
    /**
     * 处理中
     */
    Processing("Processing"),
    /**
     * 正常结束
     */
    Closed("Closed"),
    /**
     * 取消
     */
    Cancelled("Cancelled"),
    /**
     * 拒绝(三方合规拒绝)
     */
    Rejected("Rejected"),
    /**
     * 驳回
     */
    Requested("Requested"),
    /**
     * AML审核中
     */
    AMLReview("AMLReview"),
    /**
     * AML审核失败，人工审核状态
     */
    AMLManualReview("AMLManualReview"),
    /**
     * 出入金审核流程
     */
    FundReview("FundReview"),
    /**
     * 出入金人工审核
     */
    FundManualReview("FundManualReview"),
    /**
     * 流程审核结束，但是暂未实际操作转账
     */
    Reviewed("Reviewed"),
    /**
     * 客户资金审核
     */
    ClientReview("ClientReview"),
    /**
     * 客户资金审核完成后三方转账 || 22-10-25 SolidFI改成子账户模式后，复用为 调用三方转账的流程了
     */
    ClientReviewTransfer("ClientReviewTransfer"),
    /**
     * 正在换汇
     */
    DoConversion("DoConversion"),
    /**
     * 退款换汇
     */
    RefundConversion("RefundConversion"),
    /**
     * 退款换汇转入主账户
     */
    RefundConversionToQbit("RefundConversionToQbit"),
    /**
     * 退款转入用户账户
     */
    RefundConversionToUser("RefundConversionToUser"),
    /**
     * 入账合规确认账户关系
     */
    InboundRelationshipConfirm("InboundRelationshipConfirm"),
    /**
     * 入账合规确认账户关系驳回
     */
    InboundRelationshipConfirmRequest("InboundRelationshipConfirmRequest"),
    /**
     * 关系资料已提交 - 已废弃,中转到InboundReview
     */
    InboundRelationshipSubmit("InboundRelationshipSubmit"),
    /**
     * 入账审核
     */
    InboundReview("InboundReview"),
    /**
     * 入账合规提交资料
     */
    InboundComplianceUpload("InboundComplianceUpload"),
    /**
     * 内部转账新增状态
     */
    InnerTransferSubmit("InnerTransferSubmit"),
    /**
     * 入账退款转钱
     */
    InboundRefundTransfer("InboundRefundTransfer"),
    /**
     * 最终态 - 付款失败退款(订单将 Closed)
     */
    PaymentFailed("PaymentFailed"),
    /**
     * 事中风控补充资料
     */
    DroolsNeedSupplement("DroolsNeedSupplement"),
    /**
     * 用户取消订单
     */
    UserCancelled("UserCancelled");


    @JsonValue
    private final String value;

    TransactionDisplayStatusEnum(String value) {
        this.value = value;
    }
}
