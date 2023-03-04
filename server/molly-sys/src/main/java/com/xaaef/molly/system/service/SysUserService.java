package com.xaaef.molly.system.service;

import com.xaaef.molly.system.vo.UserListTenantVO;

import java.util.Set;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/9 15:33
 */

public interface SysUserService {


    /**
     * 获取 系统用户关联 租户ID
     *
     * @param userId
     * @param tenantIds
     * @return
     * @author Wang Chen Chen
     * @date 2021/7/16 17:31
     */
    Set<String> listHaveTenantIds(Long userId);


    /**
     * 获取 系统用户关联 租户ID
     *
     * @param userId
     * @return
     * @author Wang Chen Chen
     * @date 2021/7/16 17:31
     */
    UserListTenantVO listHaveTenants(Long userId);


    /**
     * 修改 系统用户关联 租户
     *
     * @param userId
     * @param tenantIds
     * @return
     * @author Wang Chen Chen
     * @date 2021/7/16 17:31
     */
    boolean updateTenant(Long userId, Set<String> tenantIds);

}
