package com.xaaef.molly.tenant;


import cn.hutool.core.util.StrUtil;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.common.util.ServletUtils;
import com.xaaef.molly.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.tenant.service.MultiTenantManager;
import com.xaaef.molly.tenant.util.TenantUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.xaaef.molly.auth.enums.OAuth2Error.*;
import static com.xaaef.molly.tenant.consts.MbpConst.X_TENANT_ID;


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

    private final MultiTenantManager tenantManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*
         * 从请求头中获取 如:
         * GET https://www.baidu.com/hello
         * x-tenant-id=master
         * */
        var tenantId = request.getHeader(X_TENANT_ID);
        if (StringUtils.isEmpty(tenantId)) {
            /*
             * 从URL地址中获取   如:
             * GET https://www.baidu.com/hello?x-tenant-id=master
             * */
            tenantId = request.getParameter(X_TENANT_ID);
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
                    TenantUtils.setTenantId(JwtSecurityUtils.getTenantId());
                    tenantId = JwtSecurityUtils.getTenantId();
                }
            } else {
                return writeError(response);
            }
        }
        // 校验租户，是否存在系统中
        if (!tenantManager.existById(tenantId)) {
            var err = StrUtil.format("租户ID {} 不存在！", tenantId);
            ServletUtils.renderError(response, JsonResult.result(TENANT_ID_DOES_NOT_EXIST.getStatus(), err));
            return false;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }


    private static boolean writeError(HttpServletResponse response) {
        var err = StrUtil.format("请求头或者URL参数中必须添加 {}", X_TENANT_ID);
        ServletUtils.renderError(response, JsonResult.result(REQUEST_MUST_ADD_TENANT_ID.getStatus(), err));
        return false;
    }

}

