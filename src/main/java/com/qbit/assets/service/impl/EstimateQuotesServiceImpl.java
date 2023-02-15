package com.qbit.assets.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.domain.entity.EstimateQuotes;
import com.qbit.assets.mapper.EstimateQuotesMapper;
import com.qbit.assets.service.EstimateQuotesService;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.ConvertEstimateQuoteDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.ConvertEstimateQuoteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author martin
 * @since 2023-02-05
 */
@Service
@Slf4j
public class EstimateQuotesServiceImpl extends ServiceImpl<EstimateQuotesMapper, EstimateQuotes> implements EstimateQuotesService {
    @Resource
    private EstimateQuotesMapper estimateQuotesMapper;

    /**
     * 交易询价
     *
     * @param body
     * @return com.qbit.assets.thirdparty.internal.okx.domain.vo.ConvertEstimateQuoteVO
     * @author martinjiang
     * @date 2023/2/15 13:59
     */
    @Override
    public ConvertEstimateQuoteVO getEstimateQuote(ConvertEstimateQuoteDTO body) {
        String amount = body.getRfqSz();
        ConvertEstimateQuoteVO estimateQuoteVO = new ConvertEstimateQuoteVO();
        //生成报价时间，Unix时间戳的毫秒数格式
        estimateQuoteVO.setQuoteTime(String.valueOf(System.currentTimeMillis()));
        //报价有效期，单位为毫秒
        estimateQuoteVO.setTtlMs("15000");
        //客户端自定义的订单标识
        estimateQuoteVO.setClQReqId(UUID.randomUUID().toString());
        //报价ID
        estimateQuoteVO.setQuoteId("quoters" + "USDT-USDC" + 17);
        estimateQuoteVO.setBaseCcy(body.getBaseCcy());
        estimateQuoteVO.setQuoteCcy(body.getQuoteCcy());
        estimateQuoteVO.setSide(body.getSide());
        //原始报价的数量
        estimateQuoteVO.setOrigRfqSz(amount);
        //实际报价的数量
        estimateQuoteVO.setRfqSz(amount);
        //报价的币种
        estimateQuoteVO.setRfqSzCcy(body.getRfqSzCcy());
        //闪兑价格，单位为计价币
        estimateQuoteVO.setCnvtPx("1.004103");
        //闪兑交易币数量
        estimateQuoteVO.setBaseSz("100");
        //闪兑计价币数量
        estimateQuoteVO.setQuoteSz("99.099");

        EstimateQuotes estimateQuotes = buildEstimateQuote(estimateQuoteVO);

        estimateQuotesMapper.insert(estimateQuotes);
        return estimateQuoteVO;
    }

    private EstimateQuotes buildEstimateQuote(ConvertEstimateQuoteVO vo) {
        EstimateQuotes data = new EstimateQuotes();
        data.setQuoteId(vo.getQuoteId());

        data.setBaseCurrency(vo.getBaseCcy());
        // 格式化base amount精度
        data.setBaseAmount(new BigDecimal(vo.getBaseSz()));
        data.setQuoteCurrency(vo.getQuoteCcy());
        // 格式化quote amount精度
        data.setQuoteAmount(new BigDecimal(vo.getQuoteSz()));

        data.setSide(vo.getSide());
        data.setRate(new BigDecimal(vo.getCnvtPx()));

        data.setRfqCurrency(vo.getRfqSzCcy());
        // 格式化rfq amount精度
        data.setRfqAmount(new BigDecimal(vo.getRfqSz()));

        data.setCreateTime(new Date());
        data.setTtlMs(Long.valueOf(vo.getTtlMs()));
        return data;
    }
}
