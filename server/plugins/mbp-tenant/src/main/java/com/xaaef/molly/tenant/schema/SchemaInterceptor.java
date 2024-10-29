package com.xaaef.molly.tenant.schema;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.xaaef.molly.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.common.util.TenantUtils;
import com.xaaef.molly.tenant.props.MultiTenantProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.ibatis.executor.statement.StatementHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xaaef.molly.common.consts.MbpConst.TENANT_IGNORE_TABLES;


/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/14 12:40
 */


@Slf4j
@AllArgsConstructor
public class SchemaInterceptor implements InnerInterceptor {


    private final MultiTenantProperties multiTenantProperties;


    @Override
    public void beforePrepare(StatementHandler sh, Connection conn, Integer transactionTimeout) {
        var mpBoundSql = PluginUtils.mpBoundSql(sh.getBoundSql());
        // 获取当前 sql 语句中的。表名称
        var tableName = getTableListName(mpBoundSql.sql());
        // 判断 表名称 是否需要过滤，即: 使用 公共库，而不是 租户 库。
        if (ignoreTable(tableName)) {
            // 切换数据库
            switchSchema(conn, multiTenantProperties.getDefaultTenantId());
        } else {
            var tenantId = getCurrentTenantId();
            log.debug("beforePrepare.tenantId: {}", tenantId);
            // 切换数据库
            switchSchema(conn, tenantId);
        }
        InnerInterceptor.super.beforePrepare(sh, conn, transactionTimeout);
    }


    private String getCurrentTenantId() {
        // 默认租户
        var defaultTenantId = multiTenantProperties.getDefaultTenantId();
        // 1.判断当前此请求，是否已经登录。 2.判断 是否使用自定义 租户Id
        if (JwtSecurityUtils.isAuthenticated() && !TenantUtils.getUseCustomTenantId()) {
            // 判断登录的用户类型。
            // 系统用户:  可以操作任何一个 租户 的数据库。
            // 租户用户:  只能操作 所在租户 的数据库
            if (JwtSecurityUtils.isMasterUser()) {
                return TenantUtils.getTenantId();
            } else {
                return JwtSecurityUtils.getTenantId();
            }
        }
        return Optional.ofNullable(TenantUtils.getTenantId())
                .orElse(defaultTenantId);
    }


    private void switchSchema(Connection conn, String schema) {
        // 切换数据库
        var sql = String.format("use %s%s", multiTenantProperties.getPrefix(), schema);
        try {
            conn.createStatement().execute(sql);
        } catch (SQLException e) {
            log.error("switchSchema: \n{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    private final static TablesNamesFinder<Statement> TABLES_NAMES_FINDER = new TablesNamesFinder<>();


    /**
     * 解析 sql 获取全部的 表名称
     */
    private static Set<String> getTableListName(String sql) {
        Statements statements = null;
        try {
            statements = CCJSqlParserUtil.parseStatements(sql);
        } catch (JSQLParserException e) {
            log.error("getTableListName: \n{}", e.getMessage());
            return Set.of();
        }
        return statements.stream()
                .map(TABLES_NAMES_FINDER::getTables)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }


    /**
     * 过滤 公共的表。
     */
    private static boolean ignoreTable(Set<String> tableName) {
        return CollectionUtil.containsAny(TENANT_IGNORE_TABLES, tableName);
    }


}
