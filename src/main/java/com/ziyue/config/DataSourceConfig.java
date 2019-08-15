package com.ziyue.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;


/**
 * 多数据源配置
 */

@Configuration
public class DataSourceConfig {
    //将配置文件中的参数自动注入给要创建的对象DruidDataSource
	@ConfigurationProperties(prefix = "spring.primary.datasource")
    @Bean(name = "dataSource")
    @Qualifier("dataSource")
    @Order(0)
    //标记为主数据源，在未声明数据源的时候，默认用主数据源
    @Primary
    public DataSource dataSource(){
       return  new DruidDataSource();
    }

    @Primary 
    @Order(0)
	@Bean(name = "jdbcTemplate")
	public JdbcTemplate jdbcTemplate( @Qualifier("dataSource") DataSource dataSource) {
	    return new JdbcTemplate(dataSource);
	}
    
    @Primary
    @Order(0)
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    
    @ConfigurationProperties(prefix = "spring.minor.datasource")
    @Bean(name = "minorDataSource")
    @Qualifier("minorDataSource")
    @Order(1)
    public DataSource minorDataSource(){
       return  new DruidDataSource();
    }
    
    @Bean(name = "minorJdbcTemplate")
    @Order(1)
	public JdbcTemplate assetJdbcTemplate(@Qualifier("minorDataSource") DataSource minorDataSource) {
	    return new JdbcTemplate(minorDataSource);
	}
    
    @Bean(name = "minorTransactionManager")
    @Order(1)
    public PlatformTransactionManager minorTransactionManager(@Qualifier("minorDataSource") DataSource minorDataSource) {
        return new DataSourceTransactionManager(minorDataSource);
    } 
}
