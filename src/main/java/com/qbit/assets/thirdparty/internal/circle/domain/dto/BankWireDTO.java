package com.qbit.assets.thirdparty.internal.circle.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * wire bank 账户创建
 *
 * @author martinjiang
 * @version 1.0
 * @date 2022/4/9 2:34 下午
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class BankWireDTO extends BaseDTO {

    /**
     * 账号
     */
    private String accountNumber;

    /**
     * iban
     */
    private String iban;

    /**
     * 银行账户的 ABA 路由号码。请注意，这必须针对银行电汇。
     */
    private String routingNumber;


    /**
     * 结算明细
     */
    private BillingDetailsDTO billingDetails;

    /**
     * 银行地址
     */
    private BankAddressDTO bankAddress;

    @Data
    public static class AddressDTO {
        /**
         * 城市
         */
        private String city;

        /**
         * 国家
         */
        private String country;

        /**
         * line1
         */
        private String line1;

        /**
         * line2(可选)
         */
        private String line2;

        /**
         * 区
         */
        private String district;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class BillingDetailsDTO extends AddressDTO {
        /**
         * 卡或银行账户持有人的全名
         */
        private String name;

        /**
         * 地址的邮政编码/邮政编码。
         */
        private String postalCode;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class BankAddressDTO extends AddressDTO {
        /**
         * 银行名称
         */
        private String bankName;

    }
}
