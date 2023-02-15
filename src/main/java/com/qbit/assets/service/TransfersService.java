package com.qbit.assets.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qbit.assets.domain.entity.Transfers;
import com.qbit.assets.thirdparty.internal.circle.domain.dto.PayoutDTO;
import com.qbit.assets.thirdparty.internal.circle.domain.dto.PayoutPageDTO;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.PayoutVO;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.ConvertTradeDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.ConvertTradeVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.MarketTickerVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author martin
 * @since 2023-02-05
 */
public interface TransfersService extends IService<Transfers> {
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

    PayoutVO payouts(PayoutDTO body);

    PayoutVO getPayout(String id);

    List<PayoutVO> payoutList(PayoutPageDTO pageDTO);
}
