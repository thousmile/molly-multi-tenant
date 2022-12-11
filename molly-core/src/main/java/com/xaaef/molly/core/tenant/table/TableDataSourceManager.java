package com.xaaef.molly.core.tenant.table;

import com.xaaef.molly.core.tenant.DataSourceManager;
import com.xaaef.molly.core.tenant.props.MultiTenantProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;


@Slf4j
@AllArgsConstructor
public class TableDataSourceManager implements DataSourceManager {

    // 默认租户的数据源
    private final DataSource dataSource;

    private final MultiTenantProperties multiTenantProperties;

    @Override
    public MultiTenantProperties getMultiTenantProperties() {
        return multiTenantProperties;
    }


    @Override
    public DataSource getDefaultDataSource() {
        return dataSource;
    }

    @Override
    public DataSource getDataSource(String tenantId) {
        return getDefaultDataSource();
    }

    /**
     * 创建表
     *
     * @author WangChenChen
     * @date 2022/12/7 21:05
     */
    @Override
    public void createTable(String tenantId) {
        log.warn("TenantId: {} No need to create table structures", tenantId);
    }


}
