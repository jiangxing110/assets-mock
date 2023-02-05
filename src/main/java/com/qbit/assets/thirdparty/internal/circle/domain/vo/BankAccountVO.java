package com.qbit.assets.thirdparty.internal.circle.domain.vo;

import lombok.Data;

/**
 * 请描述类的业务用途
 *
 * @author martinjiang
 * @version 1.0
 * @date 2022/4/9 3:01 下午
 **/
@Data
public class BankAccountVO {
    private String id;
    private String status;
    private String description;
    private String fingerprint;
    /**
     * 跟踪参考 需要在收款人的电汇参考字段中设置电汇跟踪参考。此字段可通过电汇创建期间的响应或通过银行指令端点检索。
     */
    private String trackingRef;
    /**
     * 受益人
     */
    private BeneficiaryDTO beneficiary;
    /**
     * 受益人的银行
     */
    private BeneficiaryBankDTO beneficiaryBank;

    @Data
    public static class BeneficiaryDTO {
        /**
         * 受益人名称
         */
        private String name;
        /**
         * 地址1
         */
        private String address1;
        /**
         * 地址2
         */
        private String address2;
    }

    @Data
    public static class BeneficiaryBankDTO {
        /**
         * 受益人名称
         */
        private String name;
        private String address;
        private String city;
        /**
         * 地址的邮政编码/邮政编码。
         */
        private String postalCode;
        /**
         * 国家
         */
        private String country;
        /**
         * 银行的国际代码
         */
        private String swiftCode;
        /**
         * 银行账户的 ABA 路由号码。请注意，这必须针对银行电汇。
         */
        private String routingNumber;
        /**
         * 账户
         */
        private String accountNumber;
    }
}
