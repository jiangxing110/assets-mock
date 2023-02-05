package com.qbit.assets.thirdparty.internal.circle.domain.vo;


import com.qbit.assets.thirdparty.internal.circle.annotation.IErrorCode;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.AmountBO;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.CardMetadataBO;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.CardVerificationBO;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.SourceBO;
import com.qbit.assets.thirdparty.internal.circle.enums.CirclePaymentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author LiTao litaoh@aliyun.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PaymentVO extends BaseVO implements IErrorCode {
    private String type;

    private CirclePaymentStatus status;

    private String description;

    private AmountBO amount;

    private AmountBO fees;

    private String merchantId;

    private String merchantWalletId;

    private SourceBO source;

    private PaymentVO originalPayment;

    private String reason;

    private List<PaymentVO> refunds;

    private CardVerificationBO verification;

    private CardMetadataBO metadata;

    private String errorCode;
}
