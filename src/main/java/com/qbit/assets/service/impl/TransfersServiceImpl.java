package com.qbit.assets.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.domain.entity.Transfers;
import com.qbit.assets.mapper.TransfersMapper;
import com.qbit.assets.service.TransfersService;
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
public class TransfersServiceImpl extends ServiceImpl<TransfersMapper, Transfers> implements TransfersService {

}
