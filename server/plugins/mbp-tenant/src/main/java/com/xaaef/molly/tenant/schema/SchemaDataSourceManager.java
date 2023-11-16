package com.xaaef.molly.tenant.schema;

import com.xaaef.molly.common.util.JsonUtils;
import com.xaaef.molly.tenant.DatabaseManager;
import com.xaaef.molly.tenant.props.MultiTenantProperties;
import liquibase.integration.spring.MultiTenantSpringLiquibase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 多租户 基于 模式（Schema）
 * 此方式，是 默认连接池，切换 租户的 schema，
 * 因为 mysql 数据库不支持 schema 所以只能使用 数据库 代替
 *
 * @author WangChenChen
 * @date 2022/12/11 11:29
 */


@Slf4j
@Component
@AllArgsConstructor
public class SchemaDataSourceManager implements DatabaseManager {

    // 默认租户的数据源
    private final DataSource dataSource;

    private final JdbcTemplate jdbcTemplate;

    private final MultiTenantProperties multiTenantProperties;

    private final MultiTenantSpringLiquibase mtsl;


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


    @Async
    @Override
    public void asyncUpdateTable(String tenantId, Consumer<Exception> consumer) {
        try {
            updateTable(Set.of(tenantId));
            consumer.accept(null);
        } catch (Exception ex) {
            consumer.accept(ex);
        }
    }


    /**
     * 创建表
     */
    @Override
    public synchronized void updateTable(String tenantId) throws Exception {
        updateTable(Set.of(tenantId));
    }


    /**
     * 创建表
     */
    @Override
    public synchronized void updateTable(Set<String> tenantIds) throws Exception {
        if (tenantIds == null || tenantIds.isEmpty()) {
            return;
        }
        log.info("update table tenantId: \n{}", JsonUtils.toJson(tenantIds));
        // 拼接数据库前缀。如: molly_google
        var dbNames = tenantIds.stream()
                .map(tenantId -> multiTenantProperties.getPrefix() + tenantId)
                .collect(Collectors.toList());
        // 判断数据库是否存在！不存在就创建
        dbNames.forEach(dbName -> {
            var sql = String.format("CREATE DATABASE IF NOT EXISTS %s CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;", dbName);
            jdbcTemplate.execute(sql);
        });
        mtsl.setSchemas(dbNames);
        mtsl.afterPropertiesSet();
    }


    @Override
    public synchronized void deleteTable(String tenantId) {
        log.warn("tenantId: {} delete table ...", tenantId);
        var tenantDbName = multiTenantProperties.getPrefix() + tenantId;
        var sql = String.format("DROP DATABASE %s ;", tenantDbName);
        jdbcTemplate.execute(sql);
    }


}
