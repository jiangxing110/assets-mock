package com.qbit.assets.thirdparty.internal.okx.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 删除子账号
 *
 * @author litao
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RemoveSubAccountDTO extends BaseDTO {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ⺟账⼾的资⾦密码
     */
    private String pwd;

    /**
     * ⼦账⼾名称
     */
    private String subAcct;
}
