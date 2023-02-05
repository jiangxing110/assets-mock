package com.qbit.assets.thirdparty.internal.okx.service;


import com.qbit.assets.thirdparty.internal.okx.domain.vo.MarketTickerVO;

import java.util.List;

/**
 * OkxMarketService
 *
 * @author litao
 * @date 2022/9/9 16:12
 */
public interface OkxMarketService {
    /**
     * 获取产品行情信息
     *
     * @param instType 产品类型 SPOT：币币 SWAP：永续合约 FUTURES：交割合约 OPTION：期权
     * @return {@link List<MarketTickerVO>}
     */
    List<MarketTickerVO> getTickers(String instType);

    /**
     * 获取单个产品行情信息
     * rate: 20次/2s
     *
     * @param instId 产品ID，如 BTC-USD-SWAP
     * @return {@link MarketTickerVO}
     */
    MarketTickerVO getTicker(String instId);
}
