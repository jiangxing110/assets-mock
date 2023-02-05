package com.qbit.assets.thirdparty.internal.okx.service;

import com.qbit.assets.thirdparty.internal.okx.domain.dto.CreateDepositAddressDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.CreateSubAccountDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.RemoveSubAccountDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.SubAccountDepositDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.*;

import java.util.List;
import java.util.Map;

/**
 * okx 经纪商相关接口
 *
 * @author litao
 */
public interface OkxBrokerService {
    /**
     * 创建子账户
     * rate: 10次/s
     *
     * @param body {@link CreateSubAccountDTO}
     * @return {@link SubAccountVO}
     */
    SubAccountVO createSubAccount(CreateSubAccountDTO body);

    /**
     * 创建子账户ApiKey
     * rate: 10次/s
     *
     * @param body {@link CreateSubAccountDTO}
     * @return {@link SubAccountVO}
     */
    SubAccountApiKeyVO createSubAccountApiKey(SubAccountApiKeyVO body);

    /**
     * 删除子账户
     * rate: 10次/s
     *
     * @param body {@link RemoveSubAccountDTO}
     * @return {@link SubAccountVO}
     */
    SubAccountVO removeSubAccount(RemoveSubAccountDTO body);

    /**
     * 获取子账户列表
     * rate: 5次/2s
     *
     * @param params 查询参数
     * @return {@link PageVO <SubAccountVO>}
     */
    PageVO<SubAccountVO> getSubAccounts(Map<String, Object> params);

    /**
     * 创建子账户充值地址
     * tips: 独立经纪商母账户创建子账户的充值地址，每个币种最多20个充值地址
     * rate: 5次/2s
     *
     * @param body {@link CreateDepositAddressDTO}
     * @return {@link SubAccountDepositAddressVO}
     */
    SubAccountDepositAddressVO createDepositAddress(CreateDepositAddressDTO body);

    /**
     * 获取子账户的充值地址
     * rate: 6次/s
     *
     * @param subAcct 子账户
     * @param ccy     币种
     */
    List<SubAccountDepositAddressVO> getDepositAddresses(String subAcct, String ccy);

    /**
     * 查询子账户充值历史
     * rate: 1次/s
     *
     * @param dto dto
     * @return {@link List<SubAccountDepositVO>}
     */
    List<SubAccountDepositVO> subAccountDepositHistory(SubAccountDepositDTO dto);
}
