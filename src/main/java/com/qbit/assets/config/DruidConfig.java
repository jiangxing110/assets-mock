package com.qbit.assets.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * @author martinjiang
 * @description DruidConfig
 * @date 2023/2/5 01:13
 */
@Configuration
public class DruidConfig {
    /**
     * 通过ConfigurationProperties注解 获取到yml中的数据
     * 在将数据源的数据注入dataSource()中
     * 并且将返回值放入ioc容器中
     */
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    //    配置后台监控
    @Bean
    public ServletRegistrationBean<StatViewServlet> servletRegistrationBean() {
        //配置的访问路径是/druid/*
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("loginUsername", "user");
        hashMap.put("loginPassword", "123");
        hashMap.put("allow", "");//允许所有访问
        bean.setInitParameters(hashMap);
        return bean;
    }

    //    过滤器
    @Bean
    public FilterRegistrationBean webStatFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());//添加过滤器 ：springweb自带的过滤器
        HashMap<String, String> hashMap = new HashMap<>();
        //这些路径不进行统计
        hashMap.put("exclusions", "*.js,*.css,/druid/*");
        bean.setInitParameters(hashMap);
        return bean;

    }
}