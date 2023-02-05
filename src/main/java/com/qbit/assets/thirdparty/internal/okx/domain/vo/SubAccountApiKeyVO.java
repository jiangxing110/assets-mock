package com.qbit.assets.thirdparty.internal.okx.domain.vo;


import com.qbit.assets.thirdparty.internal.okx.domain.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;

@Data
public class SubAccountApiKeyVO extends BaseDTO implements Serializable {

    /**
     * 子账户名称，支持6-20位字母和数字组合（区分大小写，不支持空格符号）
     */
    private String subAcct;

    /**
     * API Key的备注
     * 不超过50位字母（区分大小写）或数字，可以是纯字母或纯数字。
     */
    private String label;

    /**
     * API Key的密码，8-32位字母数字组合，至少包含一个数字、一个大写字母、一个小写字母、一个特殊字符
     */
    private String passphrase;

    /**
     * 绑定ip地址，多个ip用半角逗号隔开，最多支持20个ip
     * 如果子账户APIKey拥有交易权限，必须绑定IP地址
     */
    private String ip;

    /**
     * API Key权限
     * read_only：只读，trade ：交易
     * 默认拥有read_only权限
     */
    private String perm;

    private String apiKey;
    private String secretKey;
}
