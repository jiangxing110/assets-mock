package com.qbit.assets.service;

import com.qbit.assets.domain.entity.CryptoAssetsTransfer;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.WithdrawalDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.SubAccountDepositVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.WithdrawalHistoryVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.WithdrawalVO;

import java.util.List;
import java.util.Map;

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

    /**
     * 链上提现
     */
    WithdrawalVO withdrawal(WithdrawalDTO body);

    /**
     * 链上提现记录
     */
    List<WithdrawalHistoryVO> getWithdrawals(Map<String, Object> params);
    
}
