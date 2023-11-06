package com.xaaef.molly.web.interceptor;


import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.xaaef.molly.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.common.util.ServletUtils;
import com.xaaef.molly.common.util.TenantUtils;
import com.xaaef.molly.internal.api.ApiCmsProjectService;
import com.xaaef.molly.internal.api.ApiSysTenantService;
import com.xaaef.molly.internal.dto.CmsProjectDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.xaaef.molly.auth.enums.OAuth2Error.NO_HAVE_PROJECT_PERMISSIONS;
import static com.xaaef.molly.common.util.TenantUtils.X_PROJECT_ID;


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
public class ProjectIdInterceptor implements HandlerInterceptor {

    private final ApiCmsProjectService projectService;

    private final ApiSysTenantService tenantService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*
         * 从请求头中获取 如:
         * GET https://www.baidu.com/hello
         * x-project-id=10001
         * */
        var projectIdStr = request.getHeader(X_PROJECT_ID);
        /*
         * 从URL地址中获取   如:
         * GET https://www.baidu.com/hello?x-project-id=10001
         * */
        var paramProjectIdStr = request.getParameter(X_PROJECT_ID);
        // URL中的 项目id。会覆盖掉请求头中的 项目id
        if (StrUtil.isEmpty(projectIdStr) || StrUtil.isNotEmpty(paramProjectIdStr)) {
            projectIdStr = paramProjectIdStr;
        }
        if (StrUtil.isNotEmpty(projectIdStr)) {
            var projectId = NumberUtil.parseLong(projectIdStr);
            TenantUtils.setProjectId(projectId);
        } else {
            // 使用 默认项目ID
            var defaultProjectId = tenantService.getByMultiTenantProperties().getDefaultProjectId();
            TenantUtils.setProjectId(defaultProjectId);
        }
        // 校验 当前用户是否 有操作此项目的权限
        if (!haveProjectPermissions(response, TenantUtils.getProjectId())) {
            return false;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }


    /**
     * 判断 当前用户是否拥有 此项目的 操作权限
     */
    private boolean haveProjectPermissions(HttpServletResponse response, Long projectId) {
        // 如果当前用户是 租户的非管理员用户。
        if (JwtSecurityUtils.isAuthenticated() && (!JwtSecurityUtils.isMasterUser() && !JwtSecurityUtils.isAdminUser())) {
            var haveProjectIds = JwtSecurityUtils.getLoginUser().getHaveProjectIds();
            if (!haveProjectIds.isEmpty() && !haveProjectIds.contains(projectId)) {
                var err = StrUtil.format("您没有 项目ID {} 的操作权限！", projectId);
                var first = haveProjectIds.stream().findFirst();
                var result = JsonResult.error(NO_HAVE_PROJECT_PERMISSIONS.getStatus(), err, CmsProjectDTO.class);
                if (first.isPresent()) {
                    var firstProject = projectService.getSimpleById(first.get());
                    result.setData(firstProject);
                }
                ServletUtils.renderError(response, result);
                return false;
            }
        }
        return true;
    }


}

