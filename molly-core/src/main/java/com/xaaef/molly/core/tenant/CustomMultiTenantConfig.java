package com.xaaef.molly.core.tenant;

import com.xaaef.molly.core.tenant.props.MultiTenantProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * <p>
 * redis 和 spring cache 配置
 * </p>
 *
 * @author Wang Chen Chen<932560435@qq.com>
 * @version 2.0
 * @date 2019/12/12 23:12
 */

@Slf4j
@Configuration
@AllArgsConstructor
@ConditionalOnClass({
        javax.sql.DataSource.class,
        liquibase.Liquibase.class
})
@EnableConfigurationProperties({MultiTenantProperties.class})
public class CustomMultiTenantConfig {


    @Bean
    public CustomTenantResolver customTenantResolver(MultiTenantProperties tenantProps) {
        return new CustomTenantResolver(tenantProps);
    }


    @Bean
    public CustomDataSources customDataSources(javax.sql.DataSource dataSource,
                                               LiquibaseProperties liquibaseProperties,
                                               MultiTenantProperties props,
                                               DataSourceProperties dataSourceProperties) {
        return new CustomDataSources(dataSource, liquibaseProperties, props, dataSourceProperties);
    }


    @Bean
    public CustomTenantProvider customTenantProvider(
            javax.sql.DataSource dataSource,
            MultiTenantProperties props) {
        return new CustomTenantProvider(dataSource, props);
    }

}

