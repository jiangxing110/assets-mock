package com.qbit.assets.thirdparty.internal.okx.service.impl;


import com.qbit.assets.common.annotation.AccessLimit;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.CreateAssetsTransferDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.WithdrawalDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.*;
import com.qbit.assets.thirdparty.internal.okx.service.OkxAssetsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * okx 资金相关接口实现
 *
 * @author litao
 */
@Slf4j
@Service
public class OkxAssetsServiceImpl extends OkxBaseServiceImpl implements OkxAssetsService {
    private final static String PREFIX = "/api/v5/asset/%s";

    @AccessLimit(seconds = 1, max = 6)
    @Override
    public List<AssetsCurrencyVO> getCurrencies(String ccy) {
        Map<String, Object> params = null;
        if (StringUtils.isNotBlank(ccy)) {
            params = new HashMap<>(1);
            params.put("ccy", ccy);
        }
        String response = get(String.format(PREFIX, "currencies"), params);
        return convertList(response, AssetsCurrencyVO.class);
    }

    @AccessLimit(seconds = 1, max = 6)
    @Override
    public List<AssetsBalanceVO> getBalances(String... currencies) {
        Map<String, Object> params = new HashMap<>(1);
        if (currencies.length > 0) {
            params.put("ccy", String.join(",", currencies));
        }
        String response = get(String.format(PREFIX, "balances"), params);
        return convertList(response, AssetsBalanceVO.class);
    }

    @AccessLimit(seconds = 1, max = 1)
    @Override
    public AssetsTransferVO transfer(CreateAssetsTransferDTO body) {
        String response = post(String.format(PREFIX, "transfer"), body);
        return convert(response, AssetsTransferVO.class);
    }

    @AccessLimit(seconds = 1, max = 6)
    @Override
    public WithdrawalVO withdrawal(WithdrawalDTO body) {
        String response = post(String.format(PREFIX, "withdrawal"), body);
        return convert(response, WithdrawalVO.class);
    }

    @Override
    public List<WithdrawalHistoryVO> getWithdrawals(Map<String, Object> params) {
        String response = get(String.format(PREFIX, "withdrawal-history"), params);
        return convertList(response, WithdrawalHistoryVO.class);
    }

    @AccessLimit(seconds = 1, max = 6)
    @Override
    public List<AssetsDepositAddressVo> getDepositAddress(String ccy) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("ccy", ccy);
        String response = get(String.format(PREFIX, "deposit-address"), params);
        log.info("deposit-address" + response);
        return convertList(response, AssetsDepositAddressVo.class);
    }
}
