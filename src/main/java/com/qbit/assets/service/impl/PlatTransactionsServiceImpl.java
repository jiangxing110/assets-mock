package com.qbit.assets.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.domain.entity.PlatTransactions;
import com.qbit.assets.mapper.PlatTransactionsMapper;
import com.qbit.assets.service.PlatTransactionsService;
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
public class PlatTransactionsServiceImpl extends ServiceImpl<PlatTransactionsMapper, PlatTransactions> implements PlatTransactionsService {

}
