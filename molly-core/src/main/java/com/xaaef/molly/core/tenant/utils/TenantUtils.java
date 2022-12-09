package com.xaaef.molly.core.tenant.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

import static com.xaaef.molly.core.tenant.constant.MultiTenantName.*;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/11/25 11:14
 */

public class TenantUtils {


    private final static ThreadLocal<String> TENANT_ID_THREAD_LOCAL = new NamedInheritableThreadLocal<>("TENANT_ID_THREAD_LOCAL");


    /**
     * 获取 租户ID
     */
    public static String getTenantId() {
        if (StringUtils.isNotBlank(TENANT_ID_THREAD_LOCAL.get())) {
            return TENANT_ID_THREAD_LOCAL.get();
        }
        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(attributes)) {
            return null;
        }
        var request = attributes.getRequest();
        return request.getHeader(X_TENANT_ID);
    }


    /**
     * 设置 租户ID
     */
    public static void setTenantId(String tenantId) {
        if (StringUtils.isNotBlank(tenantId)) {
            TENANT_ID_THREAD_LOCAL.set(tenantId);
        } else {
            TENANT_ID_THREAD_LOCAL.remove();
        }
    }


}
