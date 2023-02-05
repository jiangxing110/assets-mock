package com.qbit.assets.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.domain.base.BaseV2;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assets_wallets")
public class Wallets extends BaseV2 {

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
     * 钱包平台(默认circle. 的枚举值)
     */
    private String platform;

    /**
     * 描述
     */
    private String description;

    /**
     * 钱包id
     */
    private String balanceId;

    /**
     * 迁移之前的wallet id
     */
    private String oldWalletId;


}
