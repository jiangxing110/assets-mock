package com.qbit.assets.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.domain.base.BaseV2;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assets_payees")
public class Payees extends BaseV2 {

    private static final long serialVersionUID = 1L;

    /**
     * account_id
     */
    private String accountId;

    /**
     * address/bank account number/iban
     */
    private String accountNumber;

    /**
     * 状态
     */
    private String status;

    private String type;

    /**
     * 国家(银行开户国家)
     */
    private String country;

    private String description;

    private String trackingRef;

    private Boolean virtualAccountEnabled;

    private String fingerprint;
    

}
