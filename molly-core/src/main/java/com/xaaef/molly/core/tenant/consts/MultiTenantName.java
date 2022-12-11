package com.xaaef.molly.core.tenant.consts;

/**
 * <p>
 * 多租户配置名称
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/16 15:15
 */

public class MultiTenantName {

    // 数据库 前缀。 易于区分数据库名称
    private final static String TENANT_ID_PREFIX = "molly_";

    // http 租户请求头
    public final static String X_TENANT_ID = "x-tenant-id";

    public final static String X_PROJECT_ID = "x-project-id";

    public final static String DEFAULT_PROJECT_ID = "master";


}
