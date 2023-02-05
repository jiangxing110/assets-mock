package com.qbit.assets.thirdparty.internal.okx.service;


import com.qbit.assets.thirdparty.internal.okx.domain.dto.CreateAssetsTransferDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.WithdrawalDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.*;

import java.util.List;
import java.util.Map;

/**
 * okx 资金相关接口
 *
 * @author litao
 */
public interface OkxAssetsService {
    /**
     * 获取币种列表
     *
     * @param ccy ccy
     * @return {@link List<AssetsCurrencyVO>}
     */
    List<AssetsCurrencyVO> getCurrencies(String ccy);

    /**
     * 获取资金账户所有资产列表，查询各币种的余额、冻结和可用等信息。
     * rate: 6次/s
     *
     * @return {@link List<AssetsBalanceVO>}
     */
    List<AssetsBalanceVO> getBalances(String... ccy);

    /**
     * 资金划转
     * rate: 1 次/s
     *
     * @return {@link List<AssetsBalanceVO>}
     */
    AssetsTransferVO transfer(CreateAssetsTransferDTO body);

    /**
     * 提现
     * rate: 6次/s
     *
     * @param body {@link WithdrawalDTO}
     * @return {@link WithdrawalVO}
     */
    WithdrawalVO withdrawal(WithdrawalDTO body);

    /**
     * 获取提现记录
     * rate: 6 次/s
     *
     * @param params {@link <a href="https://www.okx.com/docs/zh/#rest-api-funding-get-withdrawal-history">请求参数</a>}
     * @return {@link List<WithdrawalHistoryVO>}
     */
    List<WithdrawalHistoryVO> getWithdrawals(Map<String, Object> params);

    List<AssetsDepositAddressVo> getDepositAddress(String ccy);
}
