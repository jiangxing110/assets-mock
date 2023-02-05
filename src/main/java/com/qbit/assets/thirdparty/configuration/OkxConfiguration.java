package com.qbit.assets.thirdparty.configuration;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * okx配置信息
 *
 * @author martinjiang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OkxConfiguration extends HostConfiguration {
    private String key;

    private String secret;

    private String passphrase;

    private String brokerCode;

    private String subAccountPassphrase;

    private String subAccountPerm;

    private String subAccountPrefix;
}
