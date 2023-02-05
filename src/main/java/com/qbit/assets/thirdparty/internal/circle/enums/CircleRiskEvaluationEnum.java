package com.qbit.assets.thirdparty.internal.circle.enums;


/**
 * @author martinjiang
 * @version 1.0
 * @className CircleRiskEvaluationEnum
 * @description 请描述类的业务用途
 * @date 2022/4/27 11:47
 **/
public enum CircleRiskEvaluationEnum {

    RISK_3001("3001", "Prohibited Issuer (Bank) Country", "禁止发行人（银行）国家"),
    RISK_3002("3002", "Prohibited Billing Address Country", "禁止的帐单地址国家"),
    RISK_3020("3020", "Fiat Account Denied (See Fiat Account for Reason)", "法币账户被拒绝（请参阅法币账户了解原因)"),
    RISK_3022("3022", "Fiat Account (Card) Evaluation Timeout. We recommend retrying.", "法币账户（卡）评估超时。我们建议重试."),
    RISK_3023("3023", "Fiat Account (Bank Account) Evaluation Timeout. We recommend retrying.", "法币账户（银行账户）评估超时。我们建议重试."),
    RISK_3026("3026", "Fiat Account in Unverified State.", "处于未验证状态的法定账户."),
    RISK_3027("3027", "Fiat Account in Suspended State.", "处于暂停状态的法定账户."),
    RISK_3030("3030", "Unsupported Bank Account Routing Number (rtn)", "不支持的银行帐户路由号码(rtn)"),
    RISK_3050("3050", "Customer suspended from Payment Processing", "客户暂停付款处理"),
    RISK_3070("3070", "Transaction exceeds the risk limits", "交易超出风险限额"),
    RISK_3071("3071", "Limit for Daily Aggregate exceeded (email)", "超出每日汇总的限制(电子邮件)"),
    RISK_3072("3072", "Limit for Daily Aggregate exceeded (fiat)", "超出每日总量的限制(法定)"),
    RISK_3075("3075", "Limit for Weekly Aggregate exceeded (email)", "超出每周汇总的限制(电子邮件)"),
    RISK_3076("3076", "Limit for Weekly Aggregate exceeded (fiat)", "超出每周总量的限制(法定)"),

    RISK_3210("3210", "Withdrawal Limit Exceeded (7 Day Default Payout Limit)", "超过提款限额（7天默认支付限额）"),
    RISK_3211("3211", "Withdrawal Limit Exceeded (7 Day Custom Payout Limit)", "超过提款限额（7天自定义支付限额）"),
    RISK_3220("3220", "Compliance Limit Exceeded", "超出合规限制"),

    RISK_3310("3310", "Fiat Account directly associated with fraudulent activity", "与欺诈活动直接相关的法币账户"),
    RISK_3311("3311", "Email Address directly associated with fraudulent activity", "与欺诈活动直接相关的电子邮件地址"),
    RISK_3320("3320", "Fiat Account associated with network fraud notification", "与网络欺诈通知相关的法币账户"),
    RISK_3321("3321", "Email Account associated with network fraud notification", "与网络欺诈通知关联的电子邮件帐户"),
    RISK_3330("3330", "Fiat Account flagged by Risk Team", "风险团队标记的法币账户"),
    RISK_3331("3331", "Email Account flagged by Risk team", "风险团队标记的电子邮件帐户"),
    RISK_3340("3340", "Fiat Account linked to previous fraudulent activity", "与之前的欺诈活动相关联的法币账户"),
    RISK_3341("3341", "Email Address linked to previous fraudulent activity", "与以前的欺诈活动相关联的电子邮件地址"),


    RISK_3100("3100", "Unsupported Return Code Response from Processor / Issuing Bank - Default", "来自处理器/发卡行的不支持的返回代码响应 - 默认"),
    RISK_3101("3101", "Invalid Return Code Response from Issuing Bank E.g Invalid Card", "发卡行的无效返回码响应，例如无效卡"),
    RISK_3102("3102", "Fraudulent Return Code Response from Issuing Bank E.g Pickup Card", "发卡行的欺诈性返回码响应，例如取卡卡"),
    RISK_3103("3103", "Redlisted Entity Return Code Response from Processor E.g Card Redlisted", "来自处理器的红名单实体返回代码响应，例如卡红名单"),
    RISK_3104("3104", "Account associated with an invalid ACH RTN", "与无效 ACH RTN 关联的帐户"),
    RISK_3105("3105", "Expired Card", "过期卡"),
    RISK_3150("3150", "Administrative return from ODFI / RDFI", "ODFI / RDFI 的行政回报"),
    RISK_3151("3151", "Return indicating ineligible account from customer / RDFI", "来自客户/RDFI 的退货表明不符合条件的账户"),
    RISK_3152("3152", "Unsupported transaction type return from customer / RDFI", "客户/RDFI 不支持的交易类型退货"),

    RISK_3501("3501", "Blocked Issuer (Bank) Country", "被封锁的发行人（银行）国家"),
    RISK_3502("3502", "Blocked Billing Address Country", "被封锁的帐单邮寄地址国家"),
    RISK_3520("3520", "Blocked Card Type E.g. Credit", "被封锁的卡类型，例如信用卡"),

    RISK_3550("3550", "Blocked Fiat (Card)", "封锁（卡）"),
    RISK_3551("3551", "Blocked Email Address", "被阻止的电子邮件地址"),
    RISK_3552("3552", "Blocked Phone Number", "被封锁的电话号码");


    private String code;
    private String reason;
    private String description;

    CircleRiskEvaluationEnum(String code, String reason, String description) {
        this.code = code;
        this.reason = reason;
        this.description = description;
    }

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

    public static CircleRiskEvaluationEnum getTranslation(String code) {
        CircleRiskEvaluationEnum result = null;
        for (CircleRiskEvaluationEnum s : values()) {
            if (s.getCode().equals(code)) {
                result = s;
                break;
            }
        }
        return result;
    }
}
