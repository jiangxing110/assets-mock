package com.qbit.assets.thirdparty.internal.circle.domain.vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

/**
 * @author litao
 */
@Data
public class Res<T> {
    private T data;

    private Integer code;

    @JsonAlias({"msg", "message"})
    private String message;
}
