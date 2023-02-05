package com.qbit.assets.service;


import com.qbit.assets.domain.dto.InnerBalanceTransferDto;
import com.qbit.assets.domain.entity.User;

/**
 * @author liang
 * @description InternalBalanceTransferService
 * @date 2022/9/21 18:38
 */
public interface InternalBalanceTransferService {

    /**
     * 内部钱包转账
     *
     * @param user
     * @param data
     */
    void internalBalanceTransfer(User user, InnerBalanceTransferDto data);
}
