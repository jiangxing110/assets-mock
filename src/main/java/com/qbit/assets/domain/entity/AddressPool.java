package com.qbit.assets.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.domain.base.BaseV2;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assets_address_pool")
public class AddressPool extends BaseV2 {

    private static final long serialVersionUID = 1L;

    /**
     * 地址
     */
    private String address;

    /**
     * 链
     */
    private String chain;

    /**
     * 子账户id
     */
    private String subAccount;

    /**
     * 币种
     */
    private String currency;

    /**
     * 地址标签
     */
    private String addressTag;

    /**
     * 描述
     */
    private String description;

    /**
     * 平台
     */
    private String plat;

    /**
     * 是否使用
     */
    private Boolean isUsed;


}
