package com.qbit.assets.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.domain.base.BaseV2;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assets_payees_banks")
public class PayeesBanks extends BaseV2 {

    private static final long serialVersionUID = 1L;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * SWIFT/BIC
     */
    private String routingNumber;

    /**
     * 开户银行地址
     */
    private String bankAddressId;

    /**
     * 持卡人/账单地址
     */
    private String billingAddressId;

    /**
     * 账户持有人名称
     */
    private String name;


}
