package com.qbit.assets.thirdparty.internal.circle.domain.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.*;
import com.qbit.assets.thirdparty.internal.circle.enums.CircleNotificationTypeEnum;
import com.qbit.assets.thirdparty.internal.circle.enums.SubscriptionType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author litao
 */
@Data
public class SubscriptionDTO implements Serializable {
    @JsonAlias("Type")
    private SubscriptionType type;

    @JsonAlias("MessageId")
    private String messageId;

    @JsonAlias("Token")
    private String token;

    @JsonAlias("TopicArn")
    private String topicArn;

    @JsonAlias("Message")
    private String message;

    @JsonAlias("SubscribeURL")
    private String subscribeUrl;

    @JsonAlias("Timestamp")
    private Date timestamp;

    @JsonAlias("SignatureVersion")
    private String signatureVersion;

    @JsonAlias("Signature")
    private String signature;

    @JsonAlias("SigningCertURL")
    private String signingCertUrl;

    @JsonAlias("UnsubscribeURL")
    private String unsubscribeUrl;

    @JsonAlias("MessageAttributes")
    private Object messageAttributes;

    @Data
    public static class MessageDTO {

        private CircleNotificationTypeEnum notificationType;

        private TransferVO transfer;

        private PayoutVO payout;

        private BankWireVO wire;

        private CardVO card;

        private PaymentVO payment;

        @JsonAlias("return")
        private ReturnVO returns;
    }
}
