package com.xaaef.molly.common.util;


import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;


/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/11/25 11:14
 */


public class TenantUtils {

    // http 租户请求头
    public final static String X_TENANT_ID = "x-tenant-id";

    // http 租户 默认项目 请求头
    public final static String X_PROJECT_ID = "x-project-id";


    private final static ThreadLocal<String> TENANT_ID_THREAD_LOCAL = new NamedInheritableThreadLocal<>("TENANT_ID_THREAD_LOCAL");


    private final static ThreadLocal<Long> PROJECT_ID_THREAD_LOCAL = new NamedInheritableThreadLocal<>("PROJECT_ID_THREAD_LOCAL");


    /**
     * 获取 租户ID
     */
    public static String getTenantId() {
        if (StrUtil.isNotBlank(TENANT_ID_THREAD_LOCAL.get())) {
            return TENANT_ID_THREAD_LOCAL.get();
        }
        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(attributes)) {
            return null;
        }
        var request = attributes.getRequest();
        var header = request.getHeader(X_TENANT_ID);
        if (StrUtil.isNotBlank(header)) {
            return header;
        }
        return request.getParameter(X_TENANT_ID);
    }


    /**
     * 设置 租户ID
     */
    public static void setTenantId(String tenantId) {
        if (StrUtil.isNotBlank(tenantId)) {
            TENANT_ID_THREAD_LOCAL.set(tenantId);
        } else {
            TENANT_ID_THREAD_LOCAL.remove();
        }
    }


    /**
     * 获取 项目ID
     */
    public static Long getProjectId() {
        if (PROJECT_ID_THREAD_LOCAL.get() != null) {
            return PROJECT_ID_THREAD_LOCAL.get();
        }
        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(attributes)) {
            return null;
        }
        var request = attributes.getRequest();
        // 先从请求头中 获取 项目id
        var projectIdStr = request.getHeader(X_PROJECT_ID);
        if (StrUtil.isEmpty(projectIdStr) || !NumberUtil.isNumber(projectIdStr)) {
            // 从请求参数中 获取 项目id
            projectIdStr = request.getParameter(X_PROJECT_ID);
        }
        if (StrUtil.isNotBlank(projectIdStr) && NumberUtil.isNumber(projectIdStr)) {
            return NumberUtil.parseLong(projectIdStr);
        }
        return null;
    }


    /**
     * 设置 项目ID
     */
    public static void setProjectId(Long projectId) {
        if (projectId != null) {
            PROJECT_ID_THREAD_LOCAL.set(projectId);
        } else {
            PROJECT_ID_THREAD_LOCAL.remove();
        }
    }


}
