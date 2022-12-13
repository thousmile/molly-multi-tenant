package com.xaaef.molly.core.tenant;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {
                "com.xaaef.molly.system.repository",
        },
        entityManagerFactoryRef = "commonTenantEntityManagerFactory",
        transactionManagerRef = "commonTenantTransactionManager")
public class JpaCommonTenantConfig {

    private final JpaProperties jpaProperties;

    private final HibernateProperties hibernateProperties;

    private final List<HibernatePropertiesCustomizer> customizers;

    private Map<String, Object> getVendorProperties() {
        // 公共的库，是不需要做租户拦截的
        customizers.removeIf(r -> r instanceof CurrentTenantIdentifierResolver);
        customizers.removeIf(r -> r instanceof MultiTenantConnectionProvider);
        var hibernateSettings = new HibernateSettings();
        if (customizers.size() > 0) {
            hibernateSettings.hibernatePropertiesCustomizers(this.customizers);
        }
        log.info("JpaCommonTenantConfig............");
        return new LinkedHashMap<>(
                this.hibernateProperties
                        .determineHibernateProperties(
                                jpaProperties.getProperties(),
                                hibernateSettings
                        )
        );
    }


    @Bean(name = "commonTenantEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean commonTenantEntityManagerFactory(
            @Qualifier("commonTenantDataSource") DataSource dataSource, EntityManagerFactoryBuilder builder) {
        return builder
                // 设置数据源
                .dataSource(dataSource)
                // 设置jpa配置
                .properties(getVendorProperties())
                // 设置实体包名
                .packages(
                        "com.xaaef.molly.system.entity"
                )
                // 设置持久化单元名，用于@PersistenceContext注解获取EntityManager时指定数据源
                .persistenceUnit("commonTenantPersistenceUnit")
                .build();
    }


    @Bean(name = "commonTenantEntityManager")
    public EntityManager commonTenantEntityManager(
            @Qualifier("commonTenantEntityManagerFactory") EntityManagerFactory factory) {
        return factory.createEntityManager();
    }


    @Bean(name = "commonTenantTransactionManager")
    public PlatformTransactionManager commonTenantTransactionManager(
            @Qualifier("commonTenantEntityManagerFactory") EntityManagerFactory factory) {
        return new JpaTransactionManager(factory);
    }


}
