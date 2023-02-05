package com.qbit.assets.thirdparty.internal.okx.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * WithdrawalDTO
 *
 * @author litao
 * @date 2022/9/9 12:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WithdrawalDTO extends BaseDTO implements Serializable {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 币种，如 USDT
     */
    private String ccy;

    /**
     * 数量
     */
    private String amt;

    /**
     * 提币方式
     * 3：内部转账
     * 4：链上提币
     */
    private String dest;

    /**
     * 如果选择链上提币，toAddr必须是认证过的数字货币地址。某些数字货币地址格式为地址:标签，如 ARDOR-7JF3-8F2E-QUWZ-CAN7F:123456
     * 如果选择内部转账，toAddr必须是接收方地址，可以是邮箱、手机或者账户名。
     */
    private String toAddr;

    /**
     * 网络手续费≥0，提币到数字货币地址所需网络手续费可通过获取币种列表接口查询
     */
    private String fee;

    /**
     * 币种链信息
     * 如USDT下有USDT-ERC20，USDT-TRC20，USDT-Omni多个链
     * 如果没有不填此参数，则默认为主链
     */
    private String chain;

    /**
     * 客户自定义ID
     * 字母（区分大小写）与数字的组合，可以是纯字母、纯数字且长度要在1-32位之间。
     */
    private String clientId;
}
