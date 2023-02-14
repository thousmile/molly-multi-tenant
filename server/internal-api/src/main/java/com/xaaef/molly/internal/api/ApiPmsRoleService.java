package com.xaaef.molly.internal.api;

import com.xaaef.molly.internal.dto.PmsRoleDTO;

import java.util.Set;


/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/2/14 10:17
 */

public interface ApiPmsRoleService {


    /**
     * 根据 用户ID 获取 关联的角色
     *
     * @author WangChenChen
     * @date 2023/2/14 10:47
     */
    Set<PmsRoleDTO> listByUserId(Long userId);


    /**
     * 根据 角色ID 获取 关联的菜单ID
     *
     * @author WangChenChen
     * @date 2023/2/14 10:47
     */
    Set<Long> listMenuIdByRoleIds(Set<Long> roleIds);


}
