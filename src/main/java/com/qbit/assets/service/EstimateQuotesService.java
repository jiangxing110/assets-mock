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

    ConvertEstimateQuoteVO getEstimateQuote(ConvertEstimateQuoteDTO body);
}
