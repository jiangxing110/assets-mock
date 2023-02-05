package com.qbit.assets.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.domain.base.BaseV2;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assets_operation_log")
public class OperationLog extends BaseV2 {

    private static final long serialVersionUID = 1L;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 操作人用户id
     */
    private String userId;

    /**
     * 商户账户id
     */
    private String accountId;

    /**
     * 操作日志需要记查询的特殊的业务类型
     */
    private String businessType;

    /**
     * 记录id(例如：名单筛查caseId)
     */
    private String recordId;

    /**
     * 之前状态
     */
    private String previousStatus;

    /**
     * 当前状态
     */
    private String currentStatus;

    /**
     * 操作类型CURD
     */
    private String type;

    /**
     * 当前原始数据
     */
    private String data;

    /**
     * 业务CURD发生时间
     */
    private Date occurrenceTime;

    /**
     * 评论
     */
    private String comment;

    /**
     * 对外的评论
     */
    private String userComment;

    /**
     * 给前端整理好的信息参数
     */
    private String message;

    /**
     * 账户类型，区分是内部、外部用户
     */
    private String operationAccountType;


}
