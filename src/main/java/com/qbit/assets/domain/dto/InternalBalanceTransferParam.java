package com.qbit.assets.domain.dto;

import com.qbit.assets.domain.entity.Balance;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liang
 * @description InternalBalanceTransferParam
 * @date 2022/9/21 18:28
 */
@Data
public class InternalBalanceTransferParam {
    /**
     * 加钱钱包
     */
    Balance addBalanceObj;
    /**
     * 减钱钱包
     */
    Balance subBalanceObj;
    /**
     * 金额
     */
    BigDecimal amount;
    /**
     * 汇率
     */
    BigDecimal rate;
}
