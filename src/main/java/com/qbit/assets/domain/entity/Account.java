package com.qbit.assets.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.domain.base.BaseV2;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author martin
 * @since 2023-02-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assets_account")
public class Account extends BaseV2 {

    private static final long serialVersionUID = 1L;

    private String remarks;

    @TableField("verified_Name")
    private String verifiedName;

    private String accountType;

    private String displayId;

    private String status;

    private String country;

    private String type;

    private String parentAccountId;

    private String verifiedNameEn;


}
