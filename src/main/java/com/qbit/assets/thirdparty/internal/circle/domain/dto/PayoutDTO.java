package com.qbit.assets.thirdparty.internal.circle.domain.dto;


import com.qbit.assets.thirdparty.internal.circle.domain.bo.AmountBO;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.DestinationBO;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.SourceBO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Send the Payout Dto
 *
 * @author martinjiang
 * @version 1.0
 * @date 2022/4/11 1:43 下午
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class PayoutDTO extends BaseDTO {

    /**
     * 目的对象
     */
    private DestinationBO destination;

    /**
     * 源头对象
     */
    private SourceBO source;

    /**
     * 数量
     */
    private AmountBO amount;

    /**
     * 元数据
     */
    private MetadataDTO metadata;

    @Data
    public static class MetadataDTO {
        /**
         * 受益人邮箱
         */
        private String beneficiaryEmail;
    }
}
