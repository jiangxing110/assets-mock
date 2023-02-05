package com.qbit.assets.thirdparty.internal.circle.domain.dto;


import com.qbit.assets.thirdparty.internal.circle.domain.bo.AmountBO;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.CardMetadataBO;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.SourceBO;
import com.qbit.assets.thirdparty.internal.circle.enums.CircleVerificationType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author LiTao litaoh@aliyun.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PaymentDTO extends BaseDTO {

    private CardMetadataBO metadata;

    private AmountBO amount;

    private SourceBO source;

    private String description;

    private CircleVerificationType verification;
}
