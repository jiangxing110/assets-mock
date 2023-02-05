package com.qbit.assets.thirdparty.internal.circle.domain.vo;

import com.qbit.assets.thirdparty.internal.circle.annotation.IErrorCode;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.AmountBO;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.DestinationBO;
import com.qbit.assets.thirdparty.internal.circle.enums.CircleTransactionStatusEnum;
import lombok.Data;

/**
 * payout 返回对象
 *
 * @author martinjiang
 * @version 1.0
 * @date 2022/4/11 2:00 下午
 **/

@Data
public class PayoutVO implements IErrorCode {
    private String id;

    /**
     * 金额
     */
    private AmountBO amount;

    /**
     * 状态：pending failed complete
     */
    private CircleTransactionStatusEnum status;

    /**
     * 钱包ID
     */
    private String sourceWalletId;

    /**
     * 目标信息
     */
    private DestinationBO destination;

    /**
     * 费用手续费
     */
    private AmountBO fees;
    private String createDate;
    private String updateDate;
    private String trackingRef;
    private String externalRef;

    /**
     * 拥有存储失败原因
     */
    private String errorCode;

}
