package com.qbit.assets.thirdparty.internal.circle.domain.vo;


import com.qbit.assets.thirdparty.internal.circle.annotation.IErrorCode;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.AmountBO;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.DestinationBO;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.SourceBO;
import com.qbit.assets.thirdparty.internal.circle.enums.CircleTransactionStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author litao
 */
@Data
public class TransferVO implements IErrorCode, Serializable {
    private String id;

    private SourceBO source;

    private DestinationBO destination;

    private AmountBO amount;

    private Object fees;

    private AmountBO totalAmount;

    private String transactionHash;

    private String errorCode;

    private CircleTransactionStatusEnum status;

    private Date createDate;
}
