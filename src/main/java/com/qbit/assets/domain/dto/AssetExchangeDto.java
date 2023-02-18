package com.qbit.assets.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author martinjiang
 * @description AssetExchangeDto
 * @date 2023/2/18 22:52
 */

@Data
public class AssetExchangeDto implements Serializable {
    /**
     * 询价id
     */
    private String quoteId;
    /**
     * 源币种不能为空
     */
    @NotNull(message = "源币种不能为空")
    private String source;
    /**
     * 目标币种不能为空
     */
    @NotNull(message = "目标币种不能为空")
    private String target;

    private BigDecimal amount;
    private BigDecimal fee;
}
