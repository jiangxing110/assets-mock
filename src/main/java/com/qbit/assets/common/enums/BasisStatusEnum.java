package com.qbit.assets.common.enums;

/**
 * @author klover
 * 基础状态
 */

public enum BasisStatusEnum {
    /**
     * 通过（终结状态）
     */
    Success,
    /**
     * 处理中
     */
    Pending,
    /**
     * 失败（终结状态-不可重新提交）
     */
    Rejected,
    /**
     * 失败（还可重新提交）
     */
    Fail,
    /**
     * 无
     */
    Na,
}
