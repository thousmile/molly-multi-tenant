package com.xaaef.molly.internal.api;

import com.xaaef.molly.internal.dto.SysTenantDTO;

import java.util.Set;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/2/14 10:17
 */

public interface ApiSysUserService {


    /**
     * 根据 用户ID 获取 租户ID
     *
     * @author WangChenChen
     * @date 2023/2/14 10:53
     */
    Set<String> listHaveTenantIds(Long userId);


}
