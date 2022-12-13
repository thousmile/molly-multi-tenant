package com.xaaef.molly.core.tenant.schema;

import com.xaaef.molly.core.tenant.DatabaseManager;
import com.xaaef.molly.core.tenant.props.MultiTenantProperties;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;


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
@ConditionalOnProperty(prefix = "multi.tenant", name = "db-style", havingValue = "Schema")
public class SchemaDataSourceManager implements DatabaseManager {

    // 默认租户的数据源
    private final DataSource dataSource;

    private final MultiTenantProperties multiTenantProperties;

    private final DataSourceProperties dataSourceProperties;


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
        log.info("tenantId: {} create table ...", tenantId);
        try {
            // 判断 schema 是否存在。不存在就创建
            var conn = dataSource.getConnection();
            // 判断数据库是否存在！不存在就创建
            String tenantDbName = multiTenantProperties.getPrefix() + tenantId;
            String sql = String.format("CREATE DATABASE IF NOT EXISTS %s CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;", tenantDbName);
            conn.createStatement().execute(sql);
            // 创建一次性的 jdbc 链接。只是用来生成表结构的。用完就关闭。
            var conn1 = new JdbcConnection(getTempConnection(tenantDbName));
            var changeLogPath = multiTenantProperties.getOtherChangeLog();
            // 使用 Liquibase 创建表结构
            if (multiTenantProperties.getOtherChangeLog().startsWith(CLASSPATH_URL_PREFIX)) {
                changeLogPath = multiTenantProperties.getOtherChangeLog().replaceFirst(CLASSPATH_URL_PREFIX, "");
            }
            var liquibase = new Liquibase(changeLogPath, new ClassLoaderResourceAccessor(), conn1);
            liquibase.update(tenantId);
            // 关闭链接
            conn1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deleteTable(String tenantId) {
        log.info("tenantId: {} delete table ...", tenantId);
        String tenantDbName = multiTenantProperties.getPrefix() + tenantId;
        String sql = String.format("DROP DATABASE %s ;", tenantDbName);
        try {
            var conn = getTempConnection(tenantDbName);
            conn.createStatement().execute(sql);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建 临时的 jdbc 连接。 用于生成表结构。用完就关闭
     *
     * @return
     * @author WangChenChen
     * @date 2022/12/8 12:44
     */
    private Connection getTempConnection(String tenantDbName) throws Exception {
        // 获取默认的数据名称
        var oldDbName = getOldDbName(dataSourceProperties.getUrl());
        // 替换连接池中的数据库名称
        var dataSourceUrl = dataSourceProperties.getUrl().replaceFirst(oldDbName, tenantDbName);
        //3.获取数据库连接对象
        return DriverManager.getConnection(dataSourceUrl,
                dataSourceProperties.getUsername(),
                dataSourceProperties.getPassword());
    }


}
