package com.qbit.assets.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.domain.entity.Wallets;
import com.qbit.assets.mapper.WalletsMapper;
import com.qbit.assets.service.WalletsService;
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
public class WalletsServiceImpl extends ServiceImpl<WalletsMapper, Wallets> implements WalletsService {

}
