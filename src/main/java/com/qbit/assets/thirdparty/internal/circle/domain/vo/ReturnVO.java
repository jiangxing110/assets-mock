package com.qbit.assets.thirdparty.internal.circle.domain.vo;

import com.qbit.assets.thirdparty.internal.circle.annotation.IErrorCode;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.AmountBO;
import com.qbit.assets.thirdparty.internal.circle.enums.CircleTransactionStatusEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * ReturnVO
 *
 * @author LiTao litaoh@aliyun.com
 */
@Data
public class ReturnVO implements Serializable, IErrorCode {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String payoutId;

    private AmountBO amount;

    private Object fees;

    private String reason;

    /**
     * 状态：pending failed complete
     */
    private CircleTransactionStatusEnum status;

    private Date createDate;

    private Date updateDate;

    /**
     * 拥有存储失败原因
     */
    private String errorCode;
}
