package com.qbit.assets.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.domain.base.BaseV2;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assets_platform_response")
public class PlatformResponse extends BaseV2 {

    private static final long serialVersionUID = 1L;

    private String thirdId;

    private String responseType;

    private String riskEvaluation;

    private String errorCode;

    private String rawData;


}
