package com.qbit.assets.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.common.enums.ChainType;
import com.qbit.assets.common.enums.CryptoAssetsPlatform;
import com.qbit.assets.domain.base.BaseV2;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author martinjiang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assets_addresse")
public class Addresse extends BaseV2 {

    private static final long serialVersionUID = 1L;

    /**
     * account_id
     */
    private String accountId;

    /**
     * 三方钱包id
     */
    private String walletId;

    /**
     * 链
     */
    private ChainType chain;

    /**
     * 币种
     */
    private String currency;

    /**
     * 钱包地址
     */
    private String address;

    /**
     * address tag
     */
    private String addressTag;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 钱包地址是否启用
     */
    private Boolean enable;

    /**
     * 是否在前端启用
     */
    private Boolean selected;

    /**
     * 平台
     */
    private CryptoAssetsPlatform platform;


    /**
     * 平台
     */
    private String subAccount;

}
