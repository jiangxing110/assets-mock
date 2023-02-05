package com.qbit.assets.thirdparty.internal.circle.domain.vo;


import com.qbit.assets.thirdparty.internal.circle.domain.bo.AmountBO;
import lombok.Data;

/**
 * mock wire payment 返回对象
 *
 * @author martinjiang
 * @version 1.0
 * @date 2022/4/9 3:27 下午
 **/
@Data
public class MockWirePaymentVO {

    /**
     * 跟踪参考 需要在收款人的电汇参考字段中设置电汇跟踪参考。
     * 此字段可通过电汇创建期间的响应或通过银行指令端点检索。
     */
    private String trackingRef;

    private AmountBO amount;

    private String status;
}
