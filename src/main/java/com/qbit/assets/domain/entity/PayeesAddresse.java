package com.qbit.assets.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.domain.base.BaseV2;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assets_payees_addresse")
public class PayeesAddresse extends BaseV2 {

    private static final long serialVersionUID = 1L;

    /**
     * 国家
     */
    private String country;

    /**
     * 城市
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 邮政编码
     */
    private String postalCode;

    private String line1;

    private String line2;


}
