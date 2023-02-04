package com.qbit.assets.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.domain.entity.Account;
import com.qbit.assets.mapper.AccountMapper;
import com.qbit.assets.service.AccountService;
import org.springframework.stereotype.Service;


/**
 * @author martinjiang
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

}
