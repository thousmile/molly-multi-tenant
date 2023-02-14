package com.xaaef.molly.internal.api;

import com.xaaef.molly.internal.dto.SysMenuDTO;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/2/14 10:17
 */

public interface ApiSysMenuService {

    /**
     * 获取 非租户的 菜单
     * 包含 平台菜单 和 公共菜单
     *
     * @author WangChenChen
     * @date 2023/2/14 10:39
     */
    Set<String> listPermsByNonTenant();


    /**
     * 获取 非租户的 菜单
     * 包含 平台菜单 和 公共菜单
     *
     * @author WangChenChen
     * @date 2023/2/14 10:39
     */
    Set<SysMenuDTO> listMenuByNonTenant();


    /**
     * 获取租户关联的菜单
     *
     * @author WangChenChen
     * @date 2023/2/14 10:39
     */
    Set<String> listPermsByTenantId(String tenantId);


    /**
     * 获取租户关联的菜单
     *
     * @author WangChenChen
     * @date 2023/2/14 10:39
     */
    Set<SysMenuDTO> listMenuByTenantId(String tenantId);


    /**
     * 根据菜单ID 获取菜单标识
     *
     * @author WangChenChen
     * @date 2023/2/14 10:39
     */
    Set<String> listPermsByMenuIds(Set<Long> menuIds);


    /**
     * 根据菜单ID 获取菜单标识
     *
     * @author WangChenChen
     * @date 2023/2/14 10:39
     */
    Set<SysMenuDTO> listMenuByMenuIds(Set<Long> menuIds);


}
