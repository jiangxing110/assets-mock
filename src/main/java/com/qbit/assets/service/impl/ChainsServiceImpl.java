package com.qbit.assets.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.domain.entity.Chains;
import com.qbit.assets.mapper.ChainsMapper;
import com.qbit.assets.service.ChainsService;
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
public class ChainsServiceImpl extends ServiceImpl<ChainsMapper, Chains> implements ChainsService {

}
