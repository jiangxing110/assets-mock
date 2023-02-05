package com.qbit.assets.common.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.qbit.assets.domain.base.BaseV2;

public class CodeGenerator {

    public static void main(String[] args) {
        AutoGenerator ag = new AutoGenerator();
        //1. 全局配置
        GlobalConfig config = new GlobalConfig();
        // 作者
        config.setAuthor("martin")
                // 文件存放位置，自定义
                .setOutputDir("/Users/martinjiang/Desktop/demo/src/main/java")
                .setFileOverride(true)
                .setIdType(IdType.AUTO)
                .setDateType(DateType.ONLY_DATE)
                // 设置生成的service接口的名字的首字母是否为I，默认Service是以I开头的
                .setServiceName("%sService")
                .setEntityName("%s")
                .setBaseResultMap(true)
                .setActiveRecord(false)
                .setBaseColumnList(true).setOpen(Boolean.TRUE);

        //2. 数据源配置
        DataSourceConfig dsConfig = new DataSourceConfig();
        // 设置数据库类型
        dsConfig.setDbType(DbType.POSTGRE_SQL)
                .setDriverName("org.postgresql.Driver")
                .setUrl("jdbc:postgresql://127.0.0.1:5432/assets_mock?useSSL=false")
                .setUsername("root")
                .setPassword("123456");


        // 映射数据库的表明
        String[] strTableNames = {"assets_transfers", "assets_operation_log"
                , "assets_user", "assets_wallets", "assets_plat_transactions", "assets_chains", "assets_balance_transaction", "assets_transaction", "assets_address_pool", "assets_addresses", "assets_currencies_pairs",
                "assets_estimate_quotes", "assets_payees", "assets_payees_addresses", "assets_platform_response", "assets_settle",
                "assets_payees_banks", "assets_platform_sub_account", "assets_share_quotas"};
        //4. 包名策略配置
        PackageConfig pkConfig = new PackageConfig();
        pkConfig.setModuleName("assets");
        pkConfig.setParent("com.assets")
                .setMapper("mapper")
                .setService("service")
                .setController("controller")
                .setEntity("domain")
                .setXml("mapper");

        //3. 策略配置globalConfiguration中
        StrategyConfig strategy = new StrategyConfig();
        strategy.setCapitalMode(true);// 开启全局大写命名
        strategy.setTablePrefix(pkConfig.getModuleName() + "_");
        strategy.setNaming(NamingStrategy.underline_to_camel);// 实体类名驼峰
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);// 属性名驼峰
        strategy.setInclude(strTableNames);// 设置要映射的表名
        strategy.setSuperEntityClass(BaseV2.class);// 生成的entity就会实现指定的Serializable接口
        strategy.setEntityLombokModel(true); // 使用 lombok
        strategy.setRestControllerStyle(true);// 开启 restful风格

        // 7、乐观锁(版本号)
        strategy.setVersionFieldName("version");
        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true);
        //5. 整合配置
        ag.setGlobalConfig(config)
                .setDataSource(dsConfig)
                .setStrategy(strategy)
                .setTemplateEngine(new VelocityTemplateEngine())
                .setPackageInfo(pkConfig);

        //6. 执行操作
        ag.execute();
        System.out.println("======= 代码自动生成完毕！ ========");
    }
}