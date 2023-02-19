package com.qbit.assets.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qbit.assets.domain.entity.Balance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * @author martinjiang
 */
@Mapper
public interface BalanceMapper extends BaseMapper<Balance> {
    /**
     * 钱包使用排它锁
     *
     * @param id
     * @return
     */
    @Select(value = "select * from assets_balance t where t.id = #{id} for update")
    Balance findBalanceByIdForUpdate(String id);

    /**
     * 钱包减少可用
     *
     * @param id
     * @param cost
     * @return
     */
    @Update(value = "update assets_balance SET available = available - #{cost}, version = version + 1, update_time = CURRENT_TIMESTAMP WHERE id = #{id} and available >= #{cost}")
    int subBalanceAvailable(@Param("id") String id, @Param("cost") BigDecimal cost);

    /**
     * 钱包减少处理中
     *
     * @param id
     * @param cost
     * @return
     */
    @Update(value = "update assets_balance SET available = available + #{cost}, version = version + 1 , update_time = CURRENT_TIMESTAMP WHERE id = #{id}")
    int addBalanceAvailable(@Param("id") String id, @Param("cost") BigDecimal cost);

    /**
     * 钱包减少处理中
     *
     * @param id
     * @param cost
     * @return
     */
    @Update(value = "update assets_balance SET pending = pending - #{cost}, version = version + 1 , update_time = CURRENT_TIMESTAMP WHERE id = #{id} and pending >= #{cost}")
    int subBalancePending(@Param("id") String id, @Param("cost") BigDecimal cost);

    /**
     * 钱包增加处理中
     *
     * @param id
     * @param cost
     * @return
     */
    @Update(value = "update assets_balance SET pending = pending + #{cost}, version = version + 1 , update_time = CURRENT_TIMESTAMP WHERE id = #{id}")
    int addBalancePending(@Param("id") String id, @Param("cost") BigDecimal cost);

    /**
     * 钱包减少冻结
     *
     * @param id
     * @param cost
     * @return
     */
    @Update(value = "update assets_balance SET frozen = frozen - #{cost}, version = version + 1 , update_time = CURRENT_TIMESTAMP WHERE id = #{id} and frozen >= #{cost}")
    int subBalanceFrozen(@Param("id") String id, @Param("cost") BigDecimal cost);

    /**
     * 钱包增加冻结
     *
     * @param id
     * @param cost
     * @return
     */
    @Update(value = "update assets_balance SET frozen = frozen + #{cost}, version = version + 1 , update_time = CURRENT_TIMESTAMP WHERE id = #{id}")
    int addBalanceFrozen(String id, BigDecimal cost);

    /**
     * admin钱包分页查询
     */
    List<Balance> balanceList(Map<String, Object> map);

    /**
     * admin钱包分页查询
     */
    int balanceListCount(Map<String, Object> map);

    /**
     * 钱包余额统计
     *
     * @return List<Balance>
     */
    List<Balance> balanceChart();


}
