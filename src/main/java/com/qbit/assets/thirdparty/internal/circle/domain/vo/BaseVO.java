package com.qbit.assets.thirdparty.internal.circle.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author LiTao litaoh@aliyun.com
 */
@Data
public class BaseVO {
    private String id;

    private Date createDate;

    private Date updateDate;
}
