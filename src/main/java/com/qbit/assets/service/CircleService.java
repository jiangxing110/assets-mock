package com.qbit.assets.service;

import com.qbit.assets.domain.entity.CryptoAssetsTransfer;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.TransferVO;

/**
 * @author martinjiang
 * @description CircleService
 * @date 2023/2/19 15:38
 */
public interface CircleService {

    /**
     * 链上充值
     */
    CryptoAssetsTransfer chainDeposit(TransferVO body);

    /**
     * 链上提现
     */
    CryptoAssetsTransfer chainWithdraw(TransferVO body);
}
