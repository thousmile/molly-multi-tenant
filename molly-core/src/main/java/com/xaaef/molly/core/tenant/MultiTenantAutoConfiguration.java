package com.xaaef.molly.core.tenant;

import com.xaaef.molly.core.tenant.database.DatabaseDataSourceManager;
import com.xaaef.molly.core.tenant.database.DatabaseTenantProvider;
import com.xaaef.molly.core.tenant.enums.DbStyle;
import com.xaaef.molly.core.tenant.props.MultiTenantProperties;
import com.xaaef.molly.core.tenant.schema.SchemaDataSourceManager;
import com.xaaef.molly.core.tenant.schema.SchemaTenantProvider;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import com.xaaef.molly.core.tenant.table.TableDataSourceManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;


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
        liquibase.Liquibase.class,
        JpaRepository.class
})
@EnableConfigurationProperties({MultiTenantProperties.class})
public class MultiTenantAutoConfiguration {


    @Bean
    public CustomTenantResolver customTenantResolver(MultiTenantProperties multiTenantProperties) {
        return new CustomTenantResolver(multiTenantProperties);
    }


    @Bean
    public DataSourceManager databaseDataSourceManager(javax.sql.DataSource dataSource,
                                                       MultiTenantProperties multiTenantProperties,
                                                       DataSourceProperties dataSourceProperties,
                                                       JdbcTemplate jdbcTemplate) {
        log.warn("databaseDataSourceManager:  {} ", multiTenantProperties.getDbStyle());
        if (multiTenantProperties.getDbStyle() == DbStyle.Schema) {
            return new SchemaDataSourceManager(dataSource, multiTenantProperties, dataSourceProperties);
        } else if (multiTenantProperties.getDbStyle() == DbStyle.DataBase) {
            return new DatabaseDataSourceManager(dataSource, multiTenantProperties, dataSourceProperties, jdbcTemplate);
        } else {
            return new TableDataSourceManager(dataSource, multiTenantProperties);
        }
    }



    @Bean
    @ConditionalOnExpression("#{'DataBase'.equalsIgnoreCase(environment.getProperty('multi.tenant.db-style'))}")
    public MultiTenantConnectionProvider databaseTenantProvider(DataSourceManager dataSourceManager) {
        log.info("databaseTenantProvider...............");
        return new DatabaseTenantProvider(dataSourceManager);
    }


    @Bean
    @ConditionalOnExpression("#{'Schema'.equalsIgnoreCase(environment.getProperty('multi.tenant.db-style'))}")
    public MultiTenantConnectionProvider schemaTenantProvider(DataSourceManager dataSourceManager) {
        log.info("schemaTenantProvider...............");
        return new SchemaTenantProvider(dataSourceManager);
    }


}

