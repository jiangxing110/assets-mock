package com.qbit.assets.thirdparty.internal.okx.service;


import com.qbit.assets.common.enums.ChainType;
import com.qbit.assets.common.enums.CryptoConversionCurrencyEnum;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.TransferVO;

import java.math.BigDecimal;

/**
 * @author martinjiang
 * @description okxRechargeService
 * @date 2022/9/27 16:15
 */
public interface OkxRechargeService {
    /**
     * okx 自动充值
     *
     * @param
     * @return com.qbit.assets.circle.domain.vo.TransferVO
     * @author martinjiang
     * @version 1.0
     * @date 2022/9/27 16:19
     */
    TransferVO autoRecharge(CryptoConversionCurrencyEnum ccy, ChainType chainType, BigDecimal balanceLimit, BigDecimal amount);
}
