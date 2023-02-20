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
    /**
     * 创建子账户
     *
     * @param body
     * @return
     */
    SubAccountVO createSubAccount(CreateSubAccountDTO body);

    /**
     * 创建子账户apikey
     *
     * @param body
     * @return
     */
    SubAccountApiKeyVO createSubAccountApiKey(SubAccountApiKeyVO body);

    /**
     * 创建子账户充值地址
     *
     * @param body
     * @return
     */
    SubAccountDepositAddressVO createDepositAddress(CreateDepositAddressDTO body);

    /**
     * 获取子账户充值地址
     */
    List<SubAccountDepositAddressVO> getDepositAddresses(String subAcct, String ccy);

    /**
     * 获取子账户充值记录
     */
    List<SubAccountDepositVO> subAccountDepositHistory(SubAccountDepositDTO body);

    /**
     * 获取币种
     */
    ConvertCurrencyPairVO getCurrencyPair(String fromCcy, String toCcy);
}
