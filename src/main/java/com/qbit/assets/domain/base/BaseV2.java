package com.qbit.assets.domain.base;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author martinjiang
 */
@Data
public class BaseV2 implements Serializable {
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    protected Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Date updateTime;

    /**
     * 删除时间
     */
    @JsonIgnore
    @TableLogic(value = "null", delval = "now()")
    protected Date deleteTime;

    /**
     * 版本号
     */
    @Version
    @JsonIgnore
    @TableField(fill = FieldFill.INSERT)
    private Integer version;
}
