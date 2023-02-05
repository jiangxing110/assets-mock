package com.qbit.assets.thirdparty.internal.circle.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author litao
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WalletDTO extends BaseDTO {
    private String description;
}
