package com.qbit.assets.thirdparty.internal.okx.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 请求基类
 *
 * @author litao
 */
@Data
public class BaseDTO implements Serializable {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;
}
