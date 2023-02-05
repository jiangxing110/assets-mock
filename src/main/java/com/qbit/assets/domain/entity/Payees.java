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
     * 收款人类型
     */
    private String type;

    /**
     * 链
     */
    private String chainType;

    /**
     * 币种
     */
    private String currency;

    private String firstName;

    private String lastName;

    /**
     * address/bank account number/iban
     */
    private String accountNumber;

    /**
     * 状态
     */
    private String status;

    private String thirdPartyId;

    private String username;

    /**
     * 同名账户
     */
    private Boolean sameName;

    /**
     * 是否是企业
     */
    private Boolean enterprise;

    /**
     * 国家(银行开户国家)
     */
    private String country;

    private String remarks;

    /**
     * 提币免验证
     */
    private Boolean trusted;


}
