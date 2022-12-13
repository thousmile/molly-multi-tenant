package com.xaaef.molly.core.tenant.table;

import com.xaaef.molly.core.tenant.DatabaseManager;
import com.xaaef.molly.core.tenant.props.MultiTenantProperties;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import javax.sql.DataSource;


@Slf4j
@AllArgsConstructor
@ConditionalOnProperty(prefix = "multi.tenant", name = "db-style", havingValue = "Table")
public class TableDataSourceManager implements DatabaseManager {

    @PostConstruct
    public void init(){
        // 执行相关业务
        log.info("multi tenant use table .....");
    }

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


    @Override
    public void deleteTable(String tenantId) {
        log.warn("TenantId: {} No need to delete table structures", tenantId);
    }


}
