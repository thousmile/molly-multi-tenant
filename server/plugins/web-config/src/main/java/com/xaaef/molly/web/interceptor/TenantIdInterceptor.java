package com.xaaef.molly.web.interceptor;


import com.xaaef.molly.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.common.domain.SmallTenant;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.common.util.ServletUtils;
import com.xaaef.molly.common.util.TenantUtils;
import com.xaaef.molly.internal.api.ApiSysTenantService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.xaaef.molly.auth.enums.OAuth2Error.*;
import static com.xaaef.molly.common.util.TenantUtils.X_TENANT_ID;


/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/11/15 11:41
 */


@Slf4j
@Component
@AllArgsConstructor
public class TenantIdInterceptor implements HandlerInterceptor {

    private final ApiSysTenantService tenantService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*
         * 从请求头中获取 如:
         * GET https://www.baidu.com/hello
         * x-tenant-id=master
         * */
        var tenantId = request.getHeader(X_TENANT_ID);
        /*
         * 从URL地址中获取   如:
         * GET https://www.baidu.com/hello?x-tenant-id=master
         * */
        var paramTenantId = request.getParameter(X_TENANT_ID);
        // URL中的租户id。会覆盖掉请求头中的租户id
        if (StringUtils.isEmpty(tenantId) || StringUtils.isNotEmpty(paramTenantId)) {
            tenantId = paramTenantId;
        }
        if (StringUtils.isEmpty(tenantId)) {
            // 判断当前此请求，是否已经登录。
            if (JwtSecurityUtils.isAuthenticated()) {
                // 判断登录的用户类型。
                // 系统用户:  必须添加 租户ID.
                // 租户用户:  租户ID 在登录的时候，已经确定了
                if (JwtSecurityUtils.isMasterUser()) {
                    return writeError(response);
                } else {
                    tenantId = JwtSecurityUtils.getTenantId();
                }
            } else {
                return writeError(response);
            }
        }
        // 校验租户，是否存在系统中
        if (!tenantService.existById(tenantId)) {
            var err = String.format("租户ID %s 不存在！", tenantId);
            ServletUtils.renderError(response, JsonResult.result(TENANT_ID_DOES_NOT_EXIST.getStatus(), err));
            return false;
        }
        TenantUtils.setTenantId(tenantId);
        // 校验 当前用户是否 有操作此租户的权限
        if (!haveTenantPermissions(response, tenantId)) {
            return false;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }


    /**
     * 判断 当前用户是否拥有 此租户的操作权限
     */
    private boolean haveTenantPermissions(HttpServletResponse response, String tenantId) {
        // 如果当前用户是 系统用户
        if (JwtSecurityUtils.isMasterUser()) {
            var haveTenantIds = JwtSecurityUtils.getLoginUser().getHaveTenantIds();
            if (!haveTenantIds.isEmpty() && !haveTenantIds.contains(tenantId)) {
                var err = String.format("您没有 租户ID %s 的操作权限！", tenantId);
                var first = haveTenantIds.stream().findFirst();
                var result = JsonResult.error(NO_HAVE_TENANT_PERMISSIONS.getStatus(), err, SmallTenant.class);
                var firstTenant = tenantService.getSmallByTenantId(first.get());
                result.setData(firstTenant);
                ServletUtils.renderError(response, result);
                return false;
            }
        }
        return true;
    }


    private static boolean writeError(HttpServletResponse response) {
        var err = String.format("请求头或者URL参数中必须添加 %s ", X_TENANT_ID);
        ServletUtils.renderError(response, JsonResult.result(REQUEST_MUST_ADD_TENANT_ID.getStatus(), err));
        return false;
    }

}

