package com.qbit.assets.thirdparty.internal.circle.domain.vo;


import com.qbit.assets.thirdparty.internal.circle.domain.bo.AmountBO;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.SourceBO;
import lombok.Data;

import java.util.List;

/**
 * payment 清单返回信息
 *
 * @author martinjiang
 * @version 1.0
 * @date 2022/4/9 3:08 下午
 **/
@Data
public class WirePaymentVO {
    private String id;

    /**
     * type:payment
     */
    private String type;

    /**
     * 商店名称ID
     */
    private String merchantId;

    /**
     * 钱包ID
     */
    private String merchantWalletId;

    /**
     * payee 源
     */
    private SourceBO source;

    private String description;
    /**
     * 数量
     */
    private AmountBO amount;

    /**
     * 费用
     */
    private AmountBO fees;

    private String status;

    /**
     * 退款
     */
    private List<?> refunds;

    private String createDate;

    private String updateDate;
}
