package com.qbit.assets.service;

import com.qbit.assets.thirdparty.internal.circle.domain.dto.PayoutDTO;
import com.qbit.assets.thirdparty.internal.circle.domain.dto.PayoutPageDTO;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.PayoutVO;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.ConvertTradeDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.ConvertTradeVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.MarketTickerVO;

import java.util.List;
import java.util.Map;

/**
 * @author martinjiang
 * @description AssetsOkxService
 * @date 2023/2/16 00:38
 */
public interface AssetsOkxService {
    /**
     * 闪兑交易
     *
     * @param body
     * @return
     */
    ConvertTradeVO trade(ConvertTradeDTO body);

    /**
     * 闪兑交易记录
     *
     * @param
     * @return
     */
    List<ConvertTradeVO> getHistory(Map<String, Object> params);

    List<MarketTickerVO> getTickers(Map map);

    /**
     * 创建payout
     *
     * @param body
     * @return com.qbit.assets.thirdparty.internal.circle.domain.vo.PayoutVO
     * @author martinjiang
     * @date 2023/2/19 22:20
     */
    PayoutVO payouts(PayoutDTO body);

    /**
     * 查询 payout 详情
     *
     * @param id
     * @return com.qbit.assets.thirdparty.internal.circle.domain.vo.PayoutVO
     * @author martinjiang
     * @date 2023/2/19 22:19
     */
    PayoutVO getPayout(String id);

    /**
     * 获取payout list
     *
     * @param pageDTO
     * @return java.util.List<com.qbit.assets.thirdparty.internal.circle.domain.vo.PayoutVO>
     * @author martinjiang
     * @date 2023/2/19 22:20
     */
    List<PayoutVO> payoutList(PayoutPageDTO pageDTO);
}
