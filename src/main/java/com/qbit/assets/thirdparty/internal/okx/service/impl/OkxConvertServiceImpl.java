package com.qbit.assets.thirdparty.internal.okx.service.impl;


import com.qbit.assets.common.annotation.AccessLimit;
import com.qbit.assets.thirdparty.HttpService;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.ConvertEstimateQuoteDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.ConvertTradeDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.ConvertCurrencyPairVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.ConvertCurrencyVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.ConvertEstimateQuoteVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.ConvertTradeVO;
import com.qbit.assets.thirdparty.internal.okx.service.OkxConvertService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * okx 闪兑相关接口实现
 *
 * @author litao
 */
@Slf4j
@Service
public class OkxConvertServiceImpl extends OkxBaseServiceImpl implements OkxConvertService {
    private final static String PREFIX = "/api/v5/asset/convert/%s";
    @Resource
    private HttpService httpService;


    @AccessLimit(seconds = 1, max = 2)
    @Override
    public List<ConvertCurrencyVO> getCurrencies() {
        String response = get(String.format(PREFIX, "currencies"), null);
        return convertList(response, ConvertCurrencyVO.class);
    }

    @AccessLimit(seconds = 1, max = 6)
    @Override
    public ConvertCurrencyPairVO getCurrencyPair(String fromCcy, String toCcy) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("fromCcy", fromCcy);
        params.put("toCcy", toCcy);
        String response = get(String.format(PREFIX, "currency-pair"), params);
        return convert(response, ConvertCurrencyPairVO.class);
    }


    @AccessLimit(seconds = 1, max = 10)
    @Override
    public ConvertEstimateQuoteVO getEstimateQuote(ConvertEstimateQuoteDTO body) {
        setBrokerCode(body);
        String response = post(String.format(PREFIX, "estimate-quote"), body);
        return convert(response, ConvertEstimateQuoteVO.class);
    }


    @AccessLimit(seconds = 1, max = 10)
    @Override
    public ConvertTradeVO trade(ConvertTradeDTO body) {
        setBrokerCode(body);
        String response = post(String.format(PREFIX, "trade"), body);
        return convert(response, ConvertTradeVO.class);
    }

    @AccessLimit(seconds = 1, max = 6)
    @Override
    public List<ConvertTradeVO> getHistory(Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>(16);
        }
        String code = getConfiguration().getBrokerCode();
        if (StringUtils.isNotBlank(code)) {
            params.put("tag", code);
        }
        String response = get(String.format(PREFIX, "history"), params);
        return convertList(response, ConvertTradeVO.class);
    }


}
