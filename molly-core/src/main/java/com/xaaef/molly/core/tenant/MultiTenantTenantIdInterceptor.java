package com.xaaef.molly.core.tenant;


import cn.hutool.core.util.StrUtil;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.common.util.ServletUtils;
import com.xaaef.molly.core.tenant.service.MultiTenantManager;
import com.xaaef.molly.core.tenant.util.TenantUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.xaaef.molly.core.tenant.consts.MultiTenantName.X_TENANT_ID;


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
public class MultiTenantTenantIdInterceptor implements HandlerInterceptor {

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
            if (StringUtils.isEmpty(tenantId)) {
                var err = StrUtil.format("请求头或者URL参数中必须添加 {}", X_TENANT_ID);
                ServletUtils.renderError(response, JsonResult.fail(err));
                return false;
            }
        }
        // 校验租户，是否存在系统中
        if (!tenantManager.existById(tenantId)) {
            var err = StrUtil.format("租户ID {} 不存在！", tenantId);
            ServletUtils.renderError(response, JsonResult.fail(err));
            return false;
        }
        TenantUtils.setTenantId(tenantId);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }


}

