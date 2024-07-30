package com.stream.nz.config.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.github.pagehelper.PageInterceptor;
import com.github.pagehelper.dialect.helper.Oracle9iDialect;
import com.github.pagehelper.page.PageAutoDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 3个interceptor方法顺序不能随意更改
 *
 * @author chen yue
 * @date 2023-03-10 10:28:46
 */
@Configuration
public class MybatisConfig {

    private static final String DIALECT_ORACLE = "oracle";

    /**
     * 新的mybatis-plus分页插件,当前指定oracle分页方式
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.ORACLE));
        return interceptor;
    }

    /**
     * 兼容PageHelper插件分页
     */
    @Bean
    public PageInterceptor pageHelper() {
        PageAutoDialect.registerDialectAlias(DIALECT_ORACLE, Oracle9iDialect.class);
        return new PageInterceptor();
    }

}
