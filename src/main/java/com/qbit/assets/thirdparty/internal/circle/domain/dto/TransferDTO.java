package com.qbit.assets.thirdparty.internal.circle.domain.dto;


import com.qbit.assets.thirdparty.internal.circle.domain.bo.AmountBO;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.DestinationBO;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.SourceBO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author litao
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TransferDTO extends BaseDTO {
    private SourceBO source;

    private DestinationBO destination;

    private AmountBO amount;
}
