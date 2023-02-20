package com.qbit.assets.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.qbit.assets.domain.entity.EstimateQuotes;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.ConvertEstimateQuoteDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.ConvertEstimateQuoteVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author martin
 * @since 2023-02-05
 */
public interface EstimateQuotesService extends IService<EstimateQuotes> {
    /**
     * 交易询价
     *
     * @param body
     * @return com.qbit.assets.thirdparty.internal.okx.domain.vo.ConvertEstimateQuoteVO
     * @author martinjiang
     * @date 2023/2/15 13:59
     */
    ConvertEstimateQuoteVO getEstimateQuote(ConvertEstimateQuoteDTO body);
}
