package com.qbit.assets.domain.dto;


import com.qbit.assets.common.enums.BalanceOperationEnum;
import com.qbit.assets.common.enums.CryptoConversionCurrencyEnum;
import com.qbit.assets.common.enums.TransactionSourceTypeEnum;
import com.qbit.assets.common.enums.TransactionTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author martinjiang
 */
@Data
@Accessors(chain = true)
public class BalanceChangeDTO implements Cloneable {
    private String accountId;

    /**
     * 需要变动的 balance Id
     */
    private String balanceId;
    /**
     * 需要变动的 币种
     */
    private CryptoConversionCurrencyEnum currency;
    /**
     * 操作类型
     */
    private BalanceOperationEnum operation;
    /**
     * 金额
     */
    private BigDecimal cost;
    /**
     * fee
     */
    private BigDecimal fee = BigDecimal.ZERO;
    /**
     * 业务表id
     */
    private String sourceId;
    /**
     *
     */
    private TransactionSourceTypeEnum sourceType;
    /**
     * 交易类型
     */
    private TransactionTypeEnum type;
    /**
     * 交易时间
     */
    private Date transactionTime;
    /**
     * 备注
     */
    private String remarks;

    @Override
    public Object clone() {
        BalanceChangeDTO balanceChangeDTO = null;
        try {
            balanceChangeDTO = (BalanceChangeDTO) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return balanceChangeDTO;
    }
}
