package com.qbit.assets.config.mybatisPlus;


import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author martinjiang
 * @description MybatisPlusConfig
 * @date 2023/2/3 15:32
 */
@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig {

    /**
     * 自定义mybatis id生成器
     *
     * @return uuid
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    /**
     * 分页插件
     *
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
