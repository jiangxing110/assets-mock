package com.qbit.assets.thirdparty.internal.okx.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 子账户信息
 *
 * @author litao
 */
@Data
public class SubAccountVO implements Serializable {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ⼦账⼾名称
     */
    private String subAcct;

    /**
     * ⼦账⼾的备注
     */
    private String label;

    /**
     * 账⼾模式 1 - 简单交易模式 2 - 单币种保证⾦模式 3 - 跨币种保证⾦模式 4 - 组合账⼾模式
     */
    private String acctLv;

    /**
     * 创建时间
     */
    private String ts;
}
