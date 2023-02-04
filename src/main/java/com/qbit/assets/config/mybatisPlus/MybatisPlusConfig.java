package com.qbit.assets.config.mybatisPlus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * @author litao
 */
@Configuration
@MapperScan("com.qbit.assets.**.mapper")
public class MybatisPlusConfig {
    /**
     * 自定义mybatis id生成器
     *
     * @return uuid
     */
    @Bean
    public IdentifierGenerator idGenerator() {
        return new IdentifierGenerator() {
            private static final IdentifierGenerator IDENTIFIER_GENERATOR = new DefaultIdentifierGenerator();

            @Override
            public Number nextId(Object entity) {
                return IDENTIFIER_GENERATOR.nextId(entity);
            }

            @Override
            public String nextUUID(Object entity) {
                return UUID.randomUUID().toString();
            }
        };
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();

        // 分页插件
        // 添加分页插件 id
        PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor();
        // 设置请求的页面大于最大页后操作，true调回到首页，false继续请求。默认false
        pageInterceptor.setOverflow(false);
        // 单页分页条数限制，默认无限制
        pageInterceptor.setMaxLimit(2500L);
        // 设置数据库类型
        pageInterceptor.setDbType(DbType.POSTGRE_SQL);
        mybatisPlusInterceptor.addInnerInterceptor(pageInterceptor);

        // 乐观锁插件
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    /**
     * 自定义Sql注入器
     *
     * @return
     */
    @Bean
    public EasySqlInjector customSqlInjector() {
        return new EasySqlInjector();
    }
}
