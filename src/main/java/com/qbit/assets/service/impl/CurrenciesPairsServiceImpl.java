package com.qbit.assets.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.domain.entity.CurrenciesPairs;
import com.qbit.assets.mapper.CurrenciesPairsMapper;
import com.qbit.assets.service.CurrenciesPairsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
public class CurrenciesPairsServiceImpl extends ServiceImpl<CurrenciesPairsMapper, CurrenciesPairs> implements CurrenciesPairsService {

}
