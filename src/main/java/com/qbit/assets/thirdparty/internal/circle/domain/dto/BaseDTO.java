package com.qbit.assets.thirdparty.internal.circle.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author litao
 */
@Data
public class BaseDTO implements Serializable {
    private String idempotencyKey;
}
