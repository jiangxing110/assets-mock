package com.qbit.assets.config.redisson;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Setter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author martinjiang
 */
@Setter
@ConfigurationProperties("spring.redis")
@Configuration
public class RedissonConfig {

    private String host;

    private String port;

    private String password;

    private Integer database;

    @Bean
    public RedissonClient redissonClient() {

        Config config = new Config();
        //单节点
        config.useSingleServer().setAddress("redis://" + host + ":" + port);
        config.useSingleServer().setDatabase(database == null ? 0 : database);
        if (StringUtils.isEmpty(password)) {
            config.useSingleServer().setPassword(null);
        } else {
            config.useSingleServer().setPassword(password);
        }
        return Redisson.create(config);
    }
}
