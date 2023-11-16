package com.xaaef.molly.tenant;


import com.xaaef.molly.tenant.props.MultiTenantProperties;
import liquibase.integration.spring.MultiTenantSpringLiquibase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;

import javax.sql.DataSource;


/**
 * <p>
 * Multi Tenant Spring Liquibase 配置
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/8 9:21
 */


@Slf4j
@Configuration
@AllArgsConstructor
public class LiquibaseConfig {

    // 默认租户的数据源
    private final DataSource dataSource;

    private final MultiTenantProperties multiTenantProperties;

    @Bean
    public MultiTenantSpringLiquibase multiTenantSpringLiquibase() {
        var liquibase1 = new MultiTenantSpringLiquibase();
        liquibase1.setDataSource(dataSource);
        liquibase1.setResourceLoader(new DefaultResourceLoader());
        liquibase1.setChangeLog(multiTenantProperties.getChangeLog());
        liquibase1.setContexts(multiTenantProperties.getContexts());
        return liquibase1;
    }

}
