package com.qbit.assets.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qbit.assets.domain.entity.Transaction;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author martin
 * @since 2023-02-05
 */
@Mapper
public interface TransactionMapper extends BaseMapper<Transaction> {

}
