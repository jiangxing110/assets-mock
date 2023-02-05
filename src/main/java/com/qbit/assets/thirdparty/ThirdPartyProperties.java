package com.qbit.assets.thirdparty;

import com.qbit.assets.thirdparty.configuration.HostConfiguration;
import com.qbit.assets.thirdparty.configuration.OkxConfiguration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * @author LiTao litaoh@aliyun.com
 */
@ConfigurationProperties(prefix = "third-party", ignoreInvalidFields = true)
@Component
@Getter
@Setter
public class ThirdPartyProperties {
    @NestedConfigurationProperty
    private HostConfiguration namesScreening;
    /**
     * okx
     */
    @NestedConfigurationProperty
    private OkxConfiguration okx;

}
