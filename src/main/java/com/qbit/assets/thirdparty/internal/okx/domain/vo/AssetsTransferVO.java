package com.qbit.assets.thirdparty.internal.okx.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 资金账号交易信息
 *
 * @author litao
 */
@Data
public class AssetsTransferVO implements Serializable {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 划转 ID
     */
    private String transId;

    /**
     * 划转币种
     */
    private String ccy;

    /**
     * 转出账户
     */
    private String from;

    /**
     * 划转量
     */
    private String amt;

    /**
     * 转入账户
     */
    private String to;

    /**
     * 客户自定义ID
     */
    private String clientId;
}
