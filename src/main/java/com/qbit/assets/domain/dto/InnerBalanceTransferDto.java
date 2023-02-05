package com.qbit.assets.domain.dto;

import lombok.Data;

/**
 * @author liang
 * @description InnerBalanceTransferDto
 * @date 2022/9/21 18:28
 */
@Data
public class InnerBalanceTransferDto {
    /**
     * 账户id
     */
    String accountId;

    /**
     * userId
     */
    String userId;

    /**
     * 增加钱的balance的id
     */
    String addBalanceId;

    /**
     * 减少钱的balance的id
     */
    String subBalanceId;

    /**
     * 转账金额
     */
    double amount;

    /**
     * 类型
     */
    String type;
}

