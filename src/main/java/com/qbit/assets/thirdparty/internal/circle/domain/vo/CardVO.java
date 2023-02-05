package com.qbit.assets.thirdparty.internal.circle.domain.vo;

import com.qbit.assets.thirdparty.internal.circle.annotation.IErrorCode;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.CardMetadataBO;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.CardVerificationBO;
import com.qbit.assets.thirdparty.internal.circle.domain.dto.BankWireDTO;
import com.qbit.assets.thirdparty.internal.circle.enums.CircleCardStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author LiTao litaoh@aliyun.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CardVO extends BaseVO implements IErrorCode {
    private CircleCardStatus status;

    private String last4;

    private String network;

    private String bin;

    private String issuerCountry;

    private String fundingType;

    private String fingerprint;

    private Integer expMonth;

    private Integer expYear;

    private BankWireDTO.BillingDetailsDTO billingDetails;

    private CardVerificationBO verification;

    private CardMetadataBO metadata;

    private String errorCode;
}
