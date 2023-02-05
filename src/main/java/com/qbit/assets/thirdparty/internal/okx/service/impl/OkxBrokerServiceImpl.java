package com.qbit.assets.thirdparty.internal.okx.service.impl;


import com.qbit.assets.common.annotation.AccessLimit;
import com.qbit.assets.common.utils.JsonUtil;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.CreateDepositAddressDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.CreateSubAccountDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.RemoveSubAccountDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.SubAccountDepositDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.*;
import com.qbit.assets.thirdparty.internal.okx.service.OkxBrokerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * okx 经纪商接口实现
 *
 * @author litao
 */
@Service
@Slf4j
public class OkxBrokerServiceImpl extends OkxBaseServiceImpl implements OkxBrokerService {
    private final static String PREFIX = "/api/v5/broker/nd/%s";
    private final static String BROKER = "/api/v5/asset/broker/nd/%s";

    /**
     * 创建子账户
     * rate: 10次/s
     *
     * @param body {@link CreateSubAccountDTO}
     * @return {@link SubAccountVO}
     */
    @AccessLimit(seconds = 1, max = 10)
    @Override
    public SubAccountVO createSubAccount(CreateSubAccountDTO body) {
        body.setPwd(getConfiguration().getPassphrase());
        String response = post(String.format(PREFIX, "create-subaccount"), body);
        log.info("create-subaccount" + response);
        return convert(response, SubAccountVO.class);
    }

    /**
     * 创建子账户ApiKey
     * rate: 10次/s
     *
     * @param body {@link CreateSubAccountDTO}
     * @return {@link SubAccountVO}
     */
    @AccessLimit(seconds = 1, max = 10)
    @Override
    public SubAccountApiKeyVO createSubAccountApiKey(SubAccountApiKeyVO body) {
        String response = post(String.format(PREFIX, "subaccount/apikey"), body);
        log.info("create-subaccount" + response);
        return convert(response, SubAccountApiKeyVO.class);
    }

    /**
     * 删除子账户
     * rate: 10次/s
     *
     * @param body {@link RemoveSubAccountDTO}
     * @return {@link SubAccountVO}
     */
    @AccessLimit(seconds = 1, max = 10)
    @Override
    public SubAccountVO removeSubAccount(RemoveSubAccountDTO body) {
        String response = post(String.format(PREFIX, "delete-subaccount"), body);
        return convert(response, SubAccountVO.class);
    }

    /**
     * 获取子账户列表
     * rate: 5次/2s
     *
     * @param params 查询参数
     * @return {@link PageVO<SubAccountVO>}
     */
    @AccessLimit(seconds = 2, max = 5)
    @Override
    public PageVO<SubAccountVO> getSubAccounts(Map<String, Object> params) {
        String response = get(String.format(PREFIX, "subaccount-info"), params);
        log.info("subaccount-info" + response);
        return convert(response, JsonUtil.getCollectionType(PageVO.class, SubAccountVO.class));
    }

    /**
     * 创建子账户充值地址
     * tips: 独立经纪商母账户创建子账户的充值地址，每个币种最多20个充值地址
     * rate: 5次/2s
     *
     * @param body {@link CreateDepositAddressDTO}
     * @return {@link SubAccountDepositAddressVO}
     */
    @AccessLimit(seconds = 2, max = 5)
    @Override
    public SubAccountDepositAddressVO createDepositAddress(CreateDepositAddressDTO body) {
        String response = post("/api/v5/asset/broker/nd/subaccount-deposit-address", body);
        return convert(response, SubAccountDepositAddressVO.class);
    }

    /**
     * 获取子账户的充值地址
     * rate: 6次/s
     *
     * @param subAcct 子账户
     * @param ccy     币种
     */
    @AccessLimit(seconds = 1, max = 6)
    @Override
    public List<SubAccountDepositAddressVO> getDepositAddresses(String subAcct, String ccy) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("subAcct", subAcct);
        params.put("ccy", ccy);
        String response = get(String.format(PREFIX, "subaccount-deposit-address"), params);
        return convertList(response, SubAccountDepositAddressVO.class);
    }

    /**
     * 查询子账户充值记录
     *
     * @param dto
     * @return java.util.List<com.qbit.thirdparty.internal.okx.domain.vo.SubAccountDepositVo>
     * @author martinjiang
     * @date 2022/9/8 14:25
     */
    @Override
    public List<SubAccountDepositVO> subAccountDepositHistory(SubAccountDepositDTO dto) {
        Map<String, Object> params = JsonUtil.toBean(dto, Map.class);
        log.info("subaccount-deposit-history request" + params);
        String response = get(String.format(BROKER, "subaccount-deposit-history"), params);
        log.info("subaccount-deposit-history" + response);
        return convertList(response, SubAccountDepositVO.class);
    }
}
