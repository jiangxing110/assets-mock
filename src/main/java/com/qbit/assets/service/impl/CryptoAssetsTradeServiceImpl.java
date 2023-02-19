package com.qbit.assets.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.common.utils.PageUtils;
import com.qbit.assets.common.utils.Query;
import com.qbit.assets.domain.entity.CryptoAssetsTrade;
import com.qbit.assets.mapper.CryptoAssetsTradeMapper;
import com.qbit.assets.service.CryptoAssetsTradeService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author EDZ
 * @description CryptoAssetsTradeServiceImpl
 * @date 2022/11/15 13:50
 */
@Service
public class CryptoAssetsTradeServiceImpl extends ServiceImpl<CryptoAssetsTradeMapper, CryptoAssetsTrade> implements CryptoAssetsTradeService {

    @Override
    public PageUtils<CryptoAssetsTrade> page(Map<String, Object> params) {
        QueryWrapper<CryptoAssetsTrade> wrapper = Query.parseWrapper(CryptoAssetsTrade.class, params);

        IPage<CryptoAssetsTrade> page = Query.getPage(CryptoAssetsTrade.class, params, "create_time");
        page = this.page(page, wrapper);
        return new PageUtils<>(page.getRecords(), (int) page.getTotal(), (int) page.getSize());
    }
}
