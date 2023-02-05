package com.qbit.assets.config.mybatisPlus;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liang
 * @description: QbitMapper
 * @date 2022/7/4 19:35
 */
public interface QbitMapper<T> extends MPJBaseMapper<T> {
    /**
     * 批量插入
     *
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(List<T> entityList);

    /**
     * 根据 id 逻辑删除数据,并带字段填充功能
     * <p>注意入参是 entity !!! ,如果字段没有自动填充,就只是单纯的逻辑删除</p>
     *
     * @param entity
     * @return
     */
    Integer deleteByIdWithFill(T entity);

    /**
     * 全量更新，不忽略null字段，等价于update
     * 解决mybatis-plus会自动忽略null字段不更新
     *
     * @param entity
     * @return
     */
    int alwaysUpdateSomeColumnById(@Param(Constants.ENTITY) T entity);
}
