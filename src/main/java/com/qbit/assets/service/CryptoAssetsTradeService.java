package com.qbit.assets.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qbit.assets.common.utils.PageUtils;
import com.qbit.assets.domain.entity.CryptoAssetsTrade;

import java.util.Map;

/**
 * @author EDZ
 * @description CryptoAssetsTrade
 * @date 2022/11/15 13:50
 */
public interface CryptoAssetsTradeService extends IService<CryptoAssetsTrade> {
    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return {@link PageUtils<CryptoAssetsTrade>}
     */
    PageUtils<CryptoAssetsTrade> page(Map<String, Object> params);
}
