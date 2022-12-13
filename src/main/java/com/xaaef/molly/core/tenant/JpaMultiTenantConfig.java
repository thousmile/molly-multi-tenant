package com.xaaef.molly.core.tenant;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
 * 多租户的 EntityManager
 * 比如: 用户，角色，部门。等. 每个租户独有的数据
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
                "com.xaaef.molly.perms.repository",
        },
        entityManagerFactoryRef = "multiTenantEntityManagerFactory",
        transactionManagerRef = "multiTenantTransactionManager")
public class JpaMultiTenantConfig {

    private final JpaProperties jpaProperties;

    private final HibernateProperties hibernateProperties;

    private final List<HibernatePropertiesCustomizer> customizers;

    private Map<String, Object> getVendorProperties() {
        var hibernateSettings = new HibernateSettings();
        if (customizers.size() > 0) {
            hibernateSettings.hibernatePropertiesCustomizers(this.customizers);
        }
        log.info("JpaMultiTenantConfig............");
        return new LinkedHashMap<>(
                this.hibernateProperties
                        .determineHibernateProperties(
                                jpaProperties.getProperties(),
                                hibernateSettings
                        )
        );
    }


    @Primary
    @Bean(name = "multiTenantEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean multiTenantEntityManagerFactory(
            @Qualifier("multiTenantDataSource") DataSource dataSource, EntityManagerFactoryBuilder builder) {
        return builder
                // 设置数据源
                .dataSource(dataSource)
                // 设置jpa配置
                .properties(getVendorProperties())
                // 设置实体包名
                .packages(
                        "com.xaaef.molly.perms.entity"
                )
                // 设置持久化单元名，用于@PersistenceContext注解获取EntityManager时指定数据源
                .persistenceUnit("multiTenantPersistenceUnit")
                .build();
    }


    @Primary
    @Bean(name = "multiTenantEntityManager")
    public EntityManager multiTenantEntityManager(
            @Qualifier("multiTenantEntityManagerFactory") EntityManagerFactory factory) {
        return factory.createEntityManager();
    }


    @Primary
    @Bean(name = "multiTenantTransactionManager")
    public PlatformTransactionManager multiTenantTransactionManager(
            @Qualifier("multiTenantEntityManagerFactory") EntityManagerFactory factory) {
        return new JpaTransactionManager(factory);
    }


}
