package com.qbit.assets.thirdparty.internal.circle.enums;

/**
 * @author martinjiang
 * @version 1.0
 * @className CircleErrorCodeEnum
 * @description 请描述类的业务用途
 * @date 2022/4/27 11:46
 **/
public enum CircleErrorCodeEnum {
    //Payout Error Codes
    insufficient_funds_payout("insufficient_funds" + "_" + CircleNotificationTypeEnum.PAYOUTS, "Exchange insufficient funds", "兑换资金不足"),
    transaction_denied("transaction_denied" + "_" + CircleNotificationTypeEnum.PAYOUTS, "The transaction was denied as the fiat account is not verified", "由于未验证法定账户，交易被拒绝"),
    transaction_failed("transaction_failed" + "_" + CircleNotificationTypeEnum.PAYOUTS, "Transaction failed due to unknown reason", "不明原因交易失败"),
    transaction_returned("transaction_returned" + "_" + CircleNotificationTypeEnum.PAYOUTS, "The transaction was returned", "交易被退回"),
    bank_transaction_error("bank_transaction_error" + "_" + CircleNotificationTypeEnum.PAYOUTS, "The bank reported an error processing the transaction", "银行报告处理交易时出错"),
    fiat_account_limit_exceeded("fiat_account_limit_exceeded" + "_" + CircleNotificationTypeEnum.PAYOUTS, "The Fiat account limit exceeded", "超过法定账户限额"),
    invalid_bank_account_number("invalid_bank_account_number" + "_" + CircleNotificationTypeEnum.PAYOUTS, "The bank account number is invalid or missing", "银行帐号无效或缺失"),
    invalid_ach_rtn("invalid_ach_rtn" + "_" + CircleNotificationTypeEnum.PAYOUTS, "The ach routing number is invalid", "各路由号码无效"),
    invalid_wire_rtn("invalid_wire_rtn" + "_" + CircleNotificationTypeEnum.PAYOUTS, "The wire routing number is invalid", "线路路由号码无效"),
    //Transfer Error Codes
    transfer_failed("transfer_failed" + "_" + CircleNotificationTypeEnum.TRANSFERS, "The transfer failed due to unknown reasons", "不明原因转账失败"),
    transfer_denied("transfer_denied" + "_" + CircleNotificationTypeEnum.TRANSFERS, "The transfer was denied by Circle Risk Service, see transfer riskEvaluation for more details", "Circle Risk Service 拒绝了转账，详情请参阅转账riskEvaluation\n"),
    blockchain_error("blockchain_error" + "_" + CircleNotificationTypeEnum.TRANSFERS, "There was an error processing the transfer on-chain", "处理链上转账时出错"),
    insufficient_funds_transfer("insufficient_funds" + "_" + CircleNotificationTypeEnum.TRANSFERS, "There was not enough funding to cover the transfer amount", "没有足够的资金来支付转账金额");
    //Transfer Error Codes


    CircleErrorCodeEnum(String code, String reason, String description) {
        this.code = code;
        this.reason = reason;
        this.description = description;
    }

    private String code;
    private String reason;
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static CircleErrorCodeEnum catchDesc(String code) {
        CircleErrorCodeEnum result = null;
        for (CircleErrorCodeEnum s : values()) {
            if (s.getCode().equals(code)) {
                result = s;
                break;
            }
        }
        return result;
    }
}
