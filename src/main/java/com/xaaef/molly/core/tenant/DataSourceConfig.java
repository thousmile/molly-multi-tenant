package com.xaaef.molly.core.tenant;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 所有租户的 EntityManager
 * 比如: 全局配置，字典集合，之类的表，使用的是公有的数据库
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/13 11:37
 */


@Slf4j
@Configuration
@AllArgsConstructor
public class DataSourceConfig {

    private final DataSourceProperties dataSourceProperties;


    @Primary
    @Bean(name = "multiTenantDataSource")
    public DataSource multiTenantDataSource() {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }



    @Bean(name = "commonTenantDataSource")
    public DataSource commonTenantDataSource() {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }



}
