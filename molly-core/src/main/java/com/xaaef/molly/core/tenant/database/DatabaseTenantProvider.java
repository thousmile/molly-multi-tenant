package com.xaaef.molly.core.tenant.database;

import com.xaaef.molly.core.tenant.DataSourceManager;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;

import static org.hibernate.cfg.AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER;


/**
 * <p>
 * 多租户 基于 数据库（DataBase）
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/11/29 15:02
 */


@Slf4j
@Component
@AllArgsConstructor
@ConditionalOnProperty(prefix = "multi.tenant", name = "db-style", havingValue = "DataBase")
public class DatabaseTenantProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl implements HibernatePropertiesCustomizer {

    @PostConstruct
    public void init() {
        log.info("multi tenant use DataBase .....");
    }

    private final DataSourceManager dataSourceManager;


    @Override
    protected DataSource selectAnyDataSource() {
        return dataSourceManager.getDefaultDataSource();
    }


    @Override
    protected DataSource selectDataSource(String tenantId) {
        if (StringUtils.isNotBlank(tenantId)) {
            return dataSourceManager.getDataSource(tenantId);
        } else {
            return this.selectAnyDataSource();
        }
    }


    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(MULTI_TENANT_CONNECTION_PROVIDER, this);
    }


}
