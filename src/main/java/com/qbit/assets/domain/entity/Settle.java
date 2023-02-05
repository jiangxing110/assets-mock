package com.qbit.assets.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.domain.base.BaseV2;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assets_settle")
public class Settle extends BaseV2 {

    private static final long serialVersionUID = 1L;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 商户账户id
     */
    private String accountId;

    /**
     * 业务类型:对账类型（USD/USDC/理财本金/理财收益）
     */
    private String settleType;

    /**
     * 余额id
     */
    private String balanceId;

    /**
     * 期初余额
     */
    private Double startBalance;

    /**
     * 期初余额
     */
    private Double endBalance;

    /**
     * 交易额
     */
    private Double appendAmount;

    /**
     * 差额
     */
    private Double difference;

    /**
     * 币种
     */
    private String currency;

    /**
     * 账单是否平的
     */
    private Boolean isFlat;

    /**
     * 对账开始时间
     */
    private Date startTime;

    /**
     * 对账结束时间
     */
    private Date endTime;

    /**
     * 三方对账是否正确
     */
    private Boolean thirdIsFlat;

    /**
     * 对账日期
     */
    private Date settleDate;

    /**
     * 对账额外信息
     */
    private String rawData;

    /**
     * 对账周期
     */
    private String settleCycle;

    /**
     * 对账文件
     */
    private String fileUrl;


}
