package com.qbit.assets.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.domain.base.BaseV2;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assets_chains")
public class Chains extends BaseV2 {

    private static final long serialVersionUID = 1L;

    /**
     * 链
     */
    private String chain;

    /**
     * 币种
     */
    private String currency;

    /**
     * 是否启用
     */
    private Boolean enable;

    /**
     * 排序
     */
    private Integer sort;


}
