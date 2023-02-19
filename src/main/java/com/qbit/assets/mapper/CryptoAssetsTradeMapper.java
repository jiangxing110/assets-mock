package com.qbit.assets.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qbit.assets.domain.entity.CryptoAssetsTrade;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author EDZ
 * @description CryptoAssetsTradeMapper
 * @date 2022/11/15 13:49
 */
@Mapper
public interface CryptoAssetsTradeMapper extends BaseMapper<CryptoAssetsTrade> {
}
