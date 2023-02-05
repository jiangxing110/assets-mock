package com.qbit.assets.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.domain.entity.Payees;
import com.qbit.assets.mapper.PayeesMapper;
import com.qbit.assets.service.PayeesService;
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
public class PayeesServiceImpl extends ServiceImpl<PayeesMapper, Payees> implements PayeesService {

}
