package com.xaaef.molly.tenant.ds;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.xaaef.molly.auth.jwt.JwtLoginUser;
import com.xaaef.molly.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.tenant.base.ParamBaseEntity;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static com.xaaef.molly.common.consts.DataScopeConst.*;

/**
 * 数据过滤处理
 */

@Aspect
@Component
public class DataScopeAspect {


    @Before("@annotation(controllerDataScope)")
    public void doBefore(JoinPoint point, DataScope controllerDataScope) throws Throwable {
        clearDataScope(point);
        handleDataScope(point, controllerDataScope);
    }


    protected void handleDataScope(final JoinPoint joinPoint, DataScope ds) {
        if (!JwtSecurityUtils.isAuthenticated()) {
            return;
        }
        // 平台用户 和 租户管理员，禁用数据权限
        if (JwtSecurityUtils.isAdminUser() || JwtSecurityUtils.isMasterUser()) {
            return;
        }
        // 获取当前的用户
        var currentUser = JwtSecurityUtils.getLoginUser();
        dataScopeFilter(joinPoint, currentUser, ds.deptAlias(), ds.userAlias());
    }


    /**
     * 数据范围过滤
     *
     * @param joinPoint 切点
     * @param user      用户
     * @param deptAlias 部门别名
     * @param userAlias 用户别名
     */
    public static void dataScopeFilter(JoinPoint joinPoint, JwtLoginUser user, String deptAlias, String userAlias) {
        StringBuilder sqlString = new StringBuilder();
        var conditions = new ArrayList<Integer>();
        var scopeCustomIds = new ArrayList<String>();
        user.getRoles().forEach(role -> {
            if (Objects.equals(DATA_SCOPE_CUSTOM, role.getDataScope())) {
                scopeCustomIds.add(Convert.toStr(role.getRoleId()));
            }
        });
        for (var role : user.getRoles()) {
            var dataScope = role.getDataScope();
            if (conditions.contains(dataScope)) {
                continue;
            }
            if (Objects.equals(DATA_SCOPE_ALL, dataScope)) {
                sqlString = new StringBuilder();
                conditions.add(dataScope);
                break;
            } else if (Objects.equals(DATA_SCOPE_CUSTOM, dataScope)) {
                if (scopeCustomIds.size() > 1) {
                    // 多个自定数据权限使用in查询，避免多次拼接。
                    sqlString.append(StrUtil.format(" OR {}.dept_id IN ( SELECT dept_id FROM pms_role_dept WHERE role_id in ({}) ) ", deptAlias, String.join(",", scopeCustomIds)));
                } else {
                    sqlString.append(StrUtil.format(" OR {}.dept_id IN ( SELECT dept_id FROM pms_role_dept WHERE role_id = {} ) ", deptAlias, role.getRoleId()));
                }
            } else if (Objects.equals(DATA_SCOPE_DEPT, dataScope)) {
                sqlString.append(StrUtil.format(" OR {}.dept_id = {} ", deptAlias, user.getDeptId()));
            } else if (Objects.equals(DATA_SCOPE_DEPT_AND_CHILD, dataScope)) {
                sqlString.append(StrUtil.format(" OR {}.dept_id IN ( SELECT dept_id FROM pms_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )", deptAlias, user.getDeptId(), user.getDeptId()));
            } else if (Objects.equals(DATA_SCOPE_SELF, dataScope)) {
                if (StrUtil.isNotBlank(userAlias)) {
                    sqlString.append(StrUtil.format(" OR {}.user_id = {} ", userAlias, user.getUserId()));
                } else {
                    // 数据权限为仅本人且没有userAlias别名不查询任何数据
                    sqlString.append(StrUtil.format(" OR {}.dept_id = 0 ", deptAlias));
                }
            }
            conditions.add(dataScope);
        }
        // 角色都不包含传递过来的权限字符，这个时候sqlString也会为空，所以要限制一下,不查询任何数据
        if (CollectionUtil.isEmpty(conditions)) {
            sqlString.append(StrUtil.format(" OR {}.dept_id = 0 ", deptAlias));
        }
        if (StrUtil.isNotBlank(sqlString.toString())) {
            var params = Arrays.stream(joinPoint.getArgs()).filter(a -> a instanceof ParamBaseEntity).findFirst();
            if (params.isPresent()) {
                var baseEntity = (ParamBaseEntity) params.get();
                baseEntity.getParamsValue().put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");
            }
        }
    }


    /**
     * 拼接权限sql前先清空params.dataScope参数防止注入
     */
    private void clearDataScope(final JoinPoint joinPoint) {
        var params = Arrays.stream(joinPoint.getArgs()).filter(a -> a instanceof ParamBaseEntity).findFirst();
        if (params.isPresent()) {
            var baseEntity = (ParamBaseEntity) params.get();
            baseEntity.getParamsValue().put(DATA_SCOPE, "");
        }
    }


}