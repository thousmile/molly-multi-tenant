package com.xaaef.molly.core.tenant;

import com.xaaef.molly.core.tenant.props.MultiTenantProperties;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.DriverManager;


@Slf4j
@AllArgsConstructor
public class CustomDataSources {

    // 默认租户的数据源
    private final DataSource dataSource;

    private final LiquibaseProperties liquibaseProperties;

    private final MultiTenantProperties props;

    private final DataSourceProperties dataSourceProperties;

    public MultiTenantProperties getProps() {
        return props;
    }

    /**
     * 创建新的 jdbc 连接。 用于生成表结构。用完就关闭
     *
     * @return
     * @author WangChenChen
     * @date 2022/12/8 12:44
     */
    private Connection getConnection(String tenantDbName) throws Exception {
        // 获取默认的数据名称
        var oldDbName = getOldDbName(dataSourceProperties.getUrl());
        // 替换连接池中的数据库名称
        var dataSourceUrl = dataSourceProperties.getUrl().replaceFirst(oldDbName, tenantDbName);
        //3.获取数据库连接对象
        return DriverManager.getConnection(dataSourceUrl,
                dataSourceProperties.getUsername(),
                dataSourceProperties.getPassword());
    }


    public static String getOldDbName(String url) {
        var startInx = url.lastIndexOf("?");
        var sub = url.substring(0, startInx);
        int startInx2 = sub.lastIndexOf("/") + 1;
        return sub.substring(startInx2);
    }


    /**
     * 创建表
     *
     * @author WangChenChen
     * @date 2022/12/7 21:05
     */
    public void createTable(String tenantId) {
        log.info("tenantId: {} create table ...", tenantId);
        try {
            // 判断 schema 是否存在。不存在就创建
            var conn = dataSource.getConnection();
            // 判断数据库是否存在！不存在就创建
            String tenantDbName = props.getPrefix() + tenantId;
            String sql = String.format("CREATE DATABASE IF NOT EXISTS %s CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;", tenantDbName);
            conn.createStatement().execute(sql);

            // 创建一次性的 jdbc 链接。只是用来生成表结构的。用完就关闭。
            var conn1 = new JdbcConnection(getConnection(tenantDbName));
            // 使用 Liquibase 创建表结构
            var liquibase = new Liquibase(liquibaseProperties.getChangeLog(), new ClassLoaderResourceAccessor(), conn1);
            liquibase.update(tenantId);
            // 关闭链接
            conn1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
