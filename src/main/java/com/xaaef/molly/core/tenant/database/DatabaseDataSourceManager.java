package com.xaaef.molly.core.tenant.database;

import com.xaaef.molly.core.tenant.DatabaseManager;
import com.xaaef.molly.core.tenant.props.MultiTenantProperties;
import jakarta.annotation.PreDestroy;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

/**
 * 多租户 基于 数据库（DataBase）
 * 此方式，是 多连接池。每一个租户都添加一个数据库连接池，在 map 中。
 * 使用的时候，根据租户 id 获取连接池
 *
 * @author WangChenChen
 * @date 2022/12/11 11:29
 */


@Slf4j
@Component
@ConditionalOnProperty(prefix = "multi.tenant", name = "db-style", havingValue = "DataBase")
public class DatabaseDataSourceManager implements DatabaseManager {

    private static final Map<String, DataSource> DATA_SOURCE_MAP = new ConcurrentHashMap<>();

    // 默认租户的数据源
    private final DataSource masterDataSource;

    private final MultiTenantProperties multiTenantProperties;

    // 数据源配置
    private final DataSourceProperties dataSourceProperties;

    private final JdbcTemplate jdbcTemplate;

    public DatabaseDataSourceManager(DataSource masterDataSource, MultiTenantProperties multiTenantProperties,
                                     DataSourceProperties dataSourceProperties, JdbcTemplate jdbcTemplate) {
        this.masterDataSource = masterDataSource;
        this.multiTenantProperties = multiTenantProperties;
        this.dataSourceProperties = dataSourceProperties;
        this.jdbcTemplate = jdbcTemplate;
        DATA_SOURCE_MAP.put(multiTenantProperties.getDefaultTenantId(), masterDataSource);
    }


    @Override
    public MultiTenantProperties getMultiTenantProperties() {
        return multiTenantProperties;
    }


    /**
     * 获取默认的数据源
     *
     * @author WangChenChen
     * @date 2022/12/7 21:05
     */
    @Override
    public DataSource getDefaultDataSource() {
        return masterDataSource;
    }


    /**
     * TODD 根据租户ID , 获取数据源
     *
     * @author WangChenChen
     * @date 2022/12/7 21:05
     */
    @Override
    public DataSource getDataSource(String tenantId) {
        if (!DATA_SOURCE_MAP.containsKey(tenantId)) {
            var datasource = createDatasource(tenantId);
            if (datasource == null) {
                return getDefaultDataSource();
            }
            DATA_SOURCE_MAP.put(tenantId, datasource);
        }
        return DATA_SOURCE_MAP.get(tenantId);
    }


    /**
     * 根据数据库名称，创建数据源
     *
     * @author WangChenChen
     * @date 2022/12/7 21:05
     */
    private DataSource createDatasource(String dbName) {
        // 判断数据库是否存在！不存在就创建
        String tenantDbName = multiTenantProperties.getPrefix() + dbName;
        String sql = String.format("CREATE DATABASE IF NOT EXISTS %s CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;", tenantDbName);
        jdbcTemplate.execute(sql);
        // 获取默认的数据名称
        var oldDbName = getOldDbName(dataSourceProperties.getUrl());
        // 替换连接池中的数据库名称
        var dataSourceUrl = dataSourceProperties.getUrl().replaceFirst(oldDbName, tenantDbName);
        log.info("createDatasource Url: {} ", dataSourceUrl);
        dataSourceProperties.setUrl(dataSourceUrl);
        dataSourceProperties.setName(String.format("HikariPool[%s]", dbName));
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }


    /**
     * 创建表
     *
     * @author WangChenChen
     * @date 2022/12/7 21:05
     */
    @Override
    public void createTable(String tenantId) {
        log.info("tenantId: {} create table ...", tenantId);
        var dataSource = getDataSource(tenantId);
        try {
            var conn = new JdbcConnection(dataSource.getConnection());
            var changeLogPath = multiTenantProperties.getOtherChangeLog();
            // 使用 Liquibase 创建表结构
            if (multiTenantProperties.getOtherChangeLog().startsWith(CLASSPATH_URL_PREFIX)) {
                changeLogPath = multiTenantProperties.getOtherChangeLog().replaceFirst(CLASSPATH_URL_PREFIX, "");
            }
            var liquibase = new Liquibase(changeLogPath, new ClassLoaderResourceAccessor(), conn);
            liquibase.update(tenantId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deleteTable(String tenantId) {
        log.info("tenantId: {} delete table ...", tenantId);
        var dataSource = getDataSource(tenantId);
        String tenantDbName = multiTenantProperties.getPrefix() + tenantId;
        String sql = String.format("DROP DATABASE %s ;", tenantDbName);
        try {
            var conn = dataSource.getConnection();
            conn.createStatement().execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (dataSource instanceof Closeable hds) {
                try {
                    hds.close();
                    log.info("Successfully close");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @PreDestroy
    public void preDestroy() {
        log.info("Close the tenant data source ...");
        DATA_SOURCE_MAP.values().forEach(r -> {
            if (masterDataSource instanceof Closeable hds) {
                try {
                    hds.close();
                    log.info("Successfully close");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
