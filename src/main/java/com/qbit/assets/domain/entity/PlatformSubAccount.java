package com.qbit.assets.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.domain.base.BaseV2;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assets_platform_sub_account")
public class PlatformSubAccount extends BaseV2 {

    private static final long serialVersionUID = 1L;

    /**
     * 账户id
     */
    private String accountId;

    /**
     * userId
     */
    private String userId;

    /**
     * 子账户
     */
    private String subAcct;

    /**
     * 子账户备注
     */
    private String label;

    /**
     * 权限:read_only: 只读; trade: 交易
     */
    private String perm;

    /**
     * appkey
     */
    private String apiKey;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * passphrase
     */
    private String passPhrase;

    /**
     * 平台
     */
    private String platform;


}
