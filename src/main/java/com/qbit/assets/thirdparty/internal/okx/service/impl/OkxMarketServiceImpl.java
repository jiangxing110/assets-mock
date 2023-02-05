package com.qbit.assets.thirdparty.internal.okx.service.impl;


import com.qbit.assets.common.annotation.AccessLimit;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.MarketTickerVO;
import com.qbit.assets.thirdparty.internal.okx.service.OkxMarketService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OkxMarketServiceImpl
 *
 * @author litao
 * @date 2022/9/9 16:14
 */
@Service
public class OkxMarketServiceImpl extends OkxBaseServiceImpl implements OkxMarketService {
    private final static String PREFIX = "/api/v5/market/%s";

    @AccessLimit(seconds = 2, max = 20)
    @Override
    public List<MarketTickerVO> getTickers(String instType) {
        Map<String, Object> params = new HashMap<>();
        params.put("instType", instType);
        String response = get(String.format(PREFIX, "tickers"), params);
        return convertList(response, MarketTickerVO.class);
    }

    @AccessLimit(seconds = 2, max = 20)
    @Override
    public MarketTickerVO getTicker(String instId) {
        Map<String, Object> params = new HashMap<>();
        params.put("instId", instId);
        String response = get(String.format(PREFIX, "ticker"), params);
        return convert(response, MarketTickerVO.class);
    }
}
