package com.qbit.assets.thirdparty.internal.okx.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 创建子账号
 *
 * @author litao
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CreateSubAccountDTO extends BaseDTO {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ⺟账⼾的资⾦密码
     * tips: 从配置文件中读取，传参时可以为空
     */
    private String pwd;

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
}
