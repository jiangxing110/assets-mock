package com.qbit.assets.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qbit.assets.common.enums.SpecialUUID;
import com.qbit.assets.common.enums.TransactionTypeEnum;
import com.qbit.assets.domain.dto.AssetTransferDto;
import com.qbit.assets.domain.entity.Balance;
import com.qbit.assets.domain.entity.CryptoAssetsTransfer;
import com.qbit.assets.service.AssetsOkxService;
import com.qbit.assets.service.BalanceService;
import com.qbit.assets.service.CryptoAssetsTransferService;
import com.qbit.assets.thirdparty.internal.circle.domain.dto.PayoutDTO;
import com.qbit.assets.thirdparty.internal.circle.domain.dto.PayoutPageDTO;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.PayoutVO;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.ConvertTradeDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.ConvertTradeVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.MarketTickerVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author martinjiang
 * @description AssetsOkxService
 * @date 2023/2/16 00:39
 */
@Service
@Slf4j
public class AssetsOkxServiceImpl implements AssetsOkxService {
    @Resource
    private CryptoAssetsTransferService cryptoAssetsTransferService;
    @Resource
    private BalanceService balanceService;

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

    /**
     * 获取产品行情
     *
     * @param map
     * @return
     */
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
        AssetTransferDto transferDto = new AssetTransferDto();
        transferDto.setType(TransactionTypeEnum.CircleWalletToWire);
        transferDto.setPayeeId(body.getDestination().getId());

        transferDto.setTradeId(body.getIdempotencyKey());
        LambdaQueryWrapper<Balance> balanceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        balanceLambdaQueryWrapper.eq(Balance::getAccountId, SpecialUUID.NullUUID.value());
        balanceLambdaQueryWrapper.eq(Balance::getWalletType, "CircleWallet");
        balanceLambdaQueryWrapper.eq(Balance::getCurrency, body.getAmount().getCurrency());
        Balance balances = balanceService.getOne(balanceLambdaQueryWrapper);
        transferDto.setOutBalanceId(balances.getId());
        BigDecimal bigDecimal = new BigDecimal(body.getAmount().getAmount());
        transferDto.setFee(new BigDecimal("25"));
        transferDto.setAmount(bigDecimal.add(transferDto.getFee()));
        CryptoAssetsTransfer cryptoAssetsTransfer = cryptoAssetsTransferService.withdraw(transferDto);

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
