package com.xaaef.molly.internal.api;

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
     * 根据租户ID 获取租户信息
     *
     * @author WangChenChen
     * @date 2023/2/14 10:53
     */
    SysTenantDTO getByTenantId(String tenantId);

    /**
     * 根据租户ID 获取租户信息
     *
     * @author WangChenChen
     * @date 2023/2/14 10:53
     */
    SysTenantDTO getSimpleByTenantId(String tenantId);


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


}
