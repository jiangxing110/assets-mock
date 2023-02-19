package com.qbit.assets.service;

import com.qbit.assets.domain.entity.CryptoAssetsTransfer;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.SubAccountDepositVO;

/**
 * @author martinjiang
 * @description OkxService
 * @date 2023/2/19 15:49
 */
public interface OkxService {
    /**
     * 链上充值
     */
    CryptoAssetsTransfer chainDeposit(SubAccountDepositVO body);
}
