package com.xaaef.molly.internal.api;

import com.xaaef.molly.common.domain.SmallTenant;
import com.xaaef.molly.internal.dto.MultiTenantPropertiesDTO;
import com.xaaef.molly.internal.dto.SysTenantDTO;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/2/14 10:17
 */

public interface ApiSysTenantService {


    /**
     * 判断 租户ID 是否存在
     *
     * @author WangChenChen
     * @date 2023/2/14 10:53
     */
    boolean existById(String tenantId);


    /**
     * 判断 租户，是否包含 项目ID 是否存在
     *
     * @author WangChenChen
     * @date 2023/2/14 10:53
     */
    boolean existTenantIncludeProjectId(String tenantId, Long projectId);


    /**
     * 租户 添加项目ID
     *
     * @author WangChenChen
     * @date 2023/2/14 10:53
     */
    void tenantAddProjectId(String tenantId, Long projectId);


    /**
     * 租户 删除项目ID
     *
     * @author WangChenChen
     * @date 2023/2/14 10:53
     */
    void tenantDelProjectId(String tenantId, Long projectId);


    /**
     * 根据租户ID 获取租户信息
     *
     * @author WangChenChen
     * @date 2023/2/14 10:53
     */
    SysTenantDTO getByTenantId(String tenantId);


    /**
     * 根据 租户ID 获取租户信息
     *
     * @author WangChenChen
     * @date 2023/2/14 10:53
     */
    SmallTenant getSmallByTenantId(String tenantId);


    /**
     * 获取当前的租户信息
     *
     * @author WangChenChen
     * @date 2023/2/14 10:53
     */
    SysTenantDTO getByCurrentTenant();


    /**
     * 获取默认的租户ID
     *
     * @author WangChenChen
     * @date 2023/2/14 10:53
     */
    String getByDefaultTenantId();


    /**
     * 获取 多租户配置
     *
     * @author WangChenChen
     * @date 2023/2/14 10:53
     */
    MultiTenantPropertiesDTO getByMultiTenantProperties();


}
