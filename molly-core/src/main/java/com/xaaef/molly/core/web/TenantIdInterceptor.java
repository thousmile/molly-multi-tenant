package com.xaaef.molly.core.web;

import cn.hutool.core.util.StrUtil;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.common.util.ServletUtils;
import com.xaaef.molly.core.tenant.utils.TenantUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.xaaef.molly.core.tenant.constant.MultiTenantName.X_TENANT_ID;


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


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*
         * 从请求头中获取 如:
         * GET https://www.baidu.com/hello
         * x-tenant-id=master
         * */
        var tenantId = request.getHeader(X_TENANT_ID);
        if (StringUtils.hasText(tenantId)) {
            log.debug("preHandle Header ===> tenantId : {}", tenantId);
            return HandlerInterceptor.super.preHandle(request, response, handler);
        } else {
            /*
             * 从URL地址中获取   如:
             * GET https://www.baidu.com/hello?x-tenant-id=master
             * */
            tenantId = request.getParameter(X_TENANT_ID);
            if (StringUtils.hasText(tenantId)) {
                log.debug("preHandle Parameter ===> tenantId : {}", tenantId);
                TenantUtils.setTenantId(tenantId);
                return HandlerInterceptor.super.preHandle(request, response, handler);
            } else {
                var err = StrUtil.format("请求头必须添加 {}", X_TENANT_ID);
                ServletUtils.renderError(response, JsonResult.fail(err));
                return false;
            }
        }
    }


}

