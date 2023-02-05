package com.qbit.assets.thirdparty.internal.circle.domain.vo;


import com.qbit.assets.thirdparty.internal.circle.domain.dto.BankWireDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建wire bank 返回信息
 *
 * @author martinjiang
 * @version 1.0
 * @date 2022/4/9 2:48 下午
 **/
@NoArgsConstructor
@Data
public class BankWireVO {

    private String id;

    private String status;

    private String description;

    private String trackingRef;

    private String fingerprint;

    private BankWireDTO.BillingDetailsDTO billingDetails;

    private BankWireDTO.BankAddressDTO bankAddress;

    private String createDate;

    private String updateDate;

    private RiskEvaluation riskEvaluation;

    @Data
    public static class RiskEvaluation {
        /**
         * 受益人邮箱
         */
        private String decision;
        /**
         * 失败原因
         */
        private String reason;
    }
}
