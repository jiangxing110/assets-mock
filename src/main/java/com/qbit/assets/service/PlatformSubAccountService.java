package com.qbit.assets.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.qbit.assets.domain.entity.PlatformSubAccount;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.CreateDepositAddressDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.CreateSubAccountDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.SubAccountDepositDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.*;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author martin
 * @since 2023-02-05
 */
public interface PlatformSubAccountService extends IService<PlatformSubAccount> {

    SubAccountVO createSubAccount(CreateSubAccountDTO body);

    SubAccountApiKeyVO createSubAccountApiKey(SubAccountApiKeyVO body);

    SubAccountDepositAddressVO createDepositAddress(CreateDepositAddressDTO body);

    List<SubAccountDepositAddressVO> getDepositAddresses(String subAcct, String ccy);

    List<SubAccountDepositVO> subAccountDepositHistory(SubAccountDepositDTO body);

    ConvertCurrencyPairVO getCurrencyPair(String fromCcy, String toCcy);
}
