package com.qbit.assets.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.domain.entity.Transfers;
import com.qbit.assets.mapper.TransfersMapper;
import com.qbit.assets.service.TransfersService;
import com.qbit.assets.thirdparty.internal.circle.domain.dto.PayoutDTO;
import com.qbit.assets.thirdparty.internal.circle.domain.dto.PayoutPageDTO;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.PayoutVO;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.ConvertTradeDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.ConvertTradeVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.MarketTickerVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author martin
 * @since 2023-02-05
 */
@Service
public class TransfersServiceImpl extends ServiceImpl<TransfersMapper, Transfers> implements TransfersService {

    @Override
    public ConvertTradeVO trade(ConvertTradeDTO body) {
        return null;
    }

    @Override
    public List<ConvertTradeVO> getHistory(Map<String, Object> params) {
        List<ConvertTradeVO> trades = new ArrayList<>();
        ConvertTradeVO convertTradeVO = new ConvertTradeVO();
        convertTradeVO.setSide("sell");
        convertTradeVO.setInstId("USDT-USDC");
        convertTradeVO.setFillPx("0.9988");
        convertTradeVO.setBaseCcy("USDT");
        convertTradeVO.setQuoteCcy("USDC");
        convertTradeVO.setFillBaseSz("0.03");
        convertTradeVO.setState("fullyFilled");
        convertTradeVO.setTradeId("traders16758564216631385");
        convertTradeVO.setFillQuoteSz("0.029964");
        convertTradeVO.setTs(System.currentTimeMillis() + "");
        trades.add(convertTradeVO);
        return trades;
    }

    @Override
    public List<MarketTickerVO> getTickers(Map map) {
        List<MarketTickerVO> marketTickers = new ArrayList<>();
        MarketTickerVO tickerVO = new MarketTickerVO();
        tickerVO.setInstType("SPOT");
        tickerVO.setInstId("USDC-USDT");
        tickerVO.setLast("0.9994");
        tickerVO.setLastSz("49.524309");
        tickerVO.setAskPx("0.9995");
        tickerVO.setAskSz("64747.280544");
        tickerVO.setBidPx("0.9994");
        tickerVO.setBidSz("71954.517155");
        tickerVO.setOpen24h("0.9992");
        tickerVO.setHigh24h("1.001");
        tickerVO.setLow24h("0.99");
        tickerVO.setVol24h("26840879.580262");
        tickerVO.setTs("1676442845103");
        tickerVO.setSodUtc0("26840879.580262");
        tickerVO.setSodUtc8("0.9994");
        marketTickers.add(tickerVO);
        return marketTickers;
    }

    @Override
    public PayoutVO payouts(PayoutDTO body) {
        return null;
    }

    @Override
    public PayoutVO getPayout(String id) {
        return null;
    }

    @Override
    public List<PayoutVO> payoutList(PayoutPageDTO pageDTO) {
        return null;
    }
}
