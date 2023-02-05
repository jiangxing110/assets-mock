package com.qbit.assets.thirdparty.internal.okx.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author martinjiang
 * @description BaseMqDto
 * @date 2022/9/15 17:08
 */
@Data
public class BaseMqDto implements Serializable {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;
    
    private String mqId;
    private Object message;
    /**
     * 消息消费次数
     */
    private int count;
}
