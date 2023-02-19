package com.qbit.assets.domain.dto;

import com.qbit.assets.common.enums.TransactionTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author martinjiang
 * @description AssetTransferDto
 * @date 2023/2/18 22:49
 */
@Data
public class AssetTransferDto implements Serializable {
    /**
     * 充值钱包ID
     */
    private String inBalanceId;

    /**
     * 提现钱包ID
     */
    private String outBalanceId;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 金额
     */
    private BigDecimal fee;

    /**
     * 交易类型
     */
    private TransactionTypeEnum type;
    /**
     * 三方业务id
     */
    private String tradeId;
}
