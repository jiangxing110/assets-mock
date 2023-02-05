package com.qbit.assets.thirdparty.internal.circle.domain.dto;


import com.qbit.assets.thirdparty.internal.circle.domain.bo.CardMetadataBO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author LiTao litaoh@aliyun.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CardDTO extends BaseDTO {
    private Integer expMonth;

    private Integer expYear;

    private String keyId;

    private String encryptedData;

    private BankWireDTO.BillingDetailsDTO billingDetails;

    private CardMetadataBO metadata;
}
