package com.qbit.assets.thirdparty.internal.okx.service;


import com.qbit.assets.thirdparty.internal.okx.domain.dto.ConvertEstimateQuoteDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.ConvertTradeDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.ConvertCurrencyPairVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.ConvertCurrencyVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.ConvertEstimateQuoteVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.ConvertTradeVO;

import java.util.List;
import java.util.Map;

/**
 * okx 闪兑相关接口
 */
public interface OkxConvertService {
    /**
     * 获取闪兑币种列表
     * rate: 6次/s
     *
     * @return {@link ConvertCurrencyVO}
     */
    List<ConvertCurrencyVO> getCurrencies();

    /**
     * 获取闪兑币对信息
     * rate: 6次/s
     *
     * @param fromCcy 消耗币种，如 USDT
     * @param toCcy   获取币种，如 BTC
     * @return {@link ConvertCurrencyPairVO}
     */
    ConvertCurrencyPairVO getCurrencyPair(String fromCcy, String toCcy);

    /**
     * 闪兑预估询价
     * rate: 2次/s
     *
     * @param body {@link ConvertEstimateQuoteDTO}
     * @return {@link ConvertEstimateQuoteVO}
     */
    ConvertEstimateQuoteVO getEstimateQuote(ConvertEstimateQuoteDTO body);

    /**
     * 闪兑交易
     * rate: 2次/s
     *
     * @param body {@link ConvertTradeDTO}
     * @return {@link ConvertTradeVO}
     */
    ConvertTradeVO trade(ConvertTradeDTO body);

    /**
     * 获取闪兑交易历史
     * rate: 6次/s
     *
     * @param params <a href="https://www.okx.com/docs/zh/#rest-api-convert-convert-trade">查询参数</a>
     * @return {@link List<ConvertTradeVO>}
     */
    List<ConvertTradeVO> getHistory(Map<String, Object> params);
}
