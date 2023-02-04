package com.qbit.assets.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qbit.assets.domain.entity.Account;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author martinjiang
 */
@Mapper
public interface AccountMapper extends BaseMapper<Account> {

}
