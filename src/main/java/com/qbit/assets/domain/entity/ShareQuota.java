package com.qbit.assets.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.domain.base.BaseV2;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assets_share_quota")
public class ShareQuota extends BaseV2 {

    private static final long serialVersionUID = 1L;

    /**
     * account_id
     */
    private String accountId;

    /**
     * 免费提现额度
     */
    private BigDecimal total;

    /**
     * 已使用额度
     */
    private BigDecimal used;


}
