package com.xaaef.molly.core.tenant.schema;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.xaaef.molly.core.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.core.tenant.props.MultiTenantProperties;
import com.xaaef.molly.core.tenant.util.TenantUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xaaef.molly.core.tenant.consts.MbpConst.TENANT_IGNORE_TABLES;


/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/14 12:40
 */


@Slf4j
@Component
@AllArgsConstructor
public class SchemaInterceptor extends JsqlParserSupport implements InnerInterceptor {


    private final MultiTenantProperties props;


    @Override
    public void beforePrepare(StatementHandler sh, Connection conn, Integer transactionTimeout) {
        var mpBoundSql = PluginUtils.mpBoundSql(sh.getBoundSql());
        // 获取当前 sql 语句中的。表名称
        Set<String> tableName = getTableListName(mpBoundSql.sql());
        // 判断 表名称 是否需要过滤，即: 使用 公共库，而不是 租户 库。
        if (ignoreTable(tableName)) {
            // 切换数据库
            switchSchema(conn, props.getDefaultTenantId());
        } else {
            var tenantId = getCurrentTenantId();
            // 切换数据库
            switchSchema(conn, tenantId);
        }
        InnerInterceptor.super.beforePrepare(sh, conn, transactionTimeout);
    }


    private String getCurrentTenantId() {
        // 如果当前是 master 库，直接放过。任何用户都可以进入 master 库，读取一些公共的数据，如: 全局配置，权限菜单
        if (StringUtils.equals(props.getDefaultTenantId(), TenantUtils.getTenantId())) {
            return TenantUtils.getTenantId();
        }
        // 如果当前已经登录了用户。如果是登录接口，就可以操作任何数据库
        if (JwtSecurityUtils.isAuthenticated()) {
            // 此用户是 master 库中的用户，就可以操作任何一个 租户的数据。
            // 否则就只能操作 自己 的数据库
            return StringUtils.equals(props.getDefaultTenantId(), JwtSecurityUtils.getTenantId()) ?
                    TenantUtils.getTenantId() :
                    JwtSecurityUtils.getTenantId();
        }
        return Optional.ofNullable(TenantUtils.getTenantId()).orElse(props.getDefaultTenantId());
    }


    private void switchSchema(Connection conn, String schema) {
        // 切换数据库
        String sql = String.format("use %s%s", props.getPrefix(), schema);
        try {
            conn.createStatement().execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private final static TablesNamesFinder TABLES_NAMES_FINDER = new TablesNamesFinder();

    /**
     * 解析 sql 获取全部的 表名称
     */
    private static Set<String> getTableListName(String sql) {
        Statements statements = null;
        try {
            statements = CCJSqlParserUtil.parseStatements(sql);
        } catch (JSQLParserException e) {
            e.printStackTrace();
            return Set.of();
        }
        return statements.getStatements()
                .stream()
                .map(TABLES_NAMES_FINDER::getTableList)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }


    private static boolean ignoreTable(Set<String> tableName) {
        return CollectionUtil.containsAny(TENANT_IGNORE_TABLES, tableName);
    }


}
