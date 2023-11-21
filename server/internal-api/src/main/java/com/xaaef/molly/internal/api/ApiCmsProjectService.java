package com.xaaef.molly.internal.api;

import com.xaaef.molly.internal.dto.CmsProjectDTO;
import com.xaaef.molly.internal.dto.SysTenantDTO;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


/**
 * <p>
 * 项目
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/2/14 10:17
 */

public interface ApiCmsProjectService {

    /**
     * 根据 项目ID 获取简单的项目信息。如：id，名称，
     *
     * @author WangChenChen
     * @date 2023/8/11 10:47
     */
    CmsProjectDTO getSimpleById(Long projectId);


    /**
     * 创建租户  初始化项目
     *
     * @return
     * @author WangChenChen
     * @date 2023/2/14 10:53
     */
    void initProject(SysTenantDTO po);


    /**
     * 根据 部门ID 获取 关联的 项目列表
     *
     * @author WangChenChen
     * @date 2023/8/11 10:47
     */
    long countProjectByDeptId(Long deptId);


    /**
     * 根据 部门ID 获取 关联的 项目列表
     *
     * @author WangChenChen
     * @date 2023/8/11 10:47
     */
    Set<Long> listProjectByDeptId(Long deptId);


    /**
     * 根据 租户数据前缀 + 租户ID 获取项目列表
     *
     * @date 2023/11/21 15:33
     */
    Map<String, Set<Long>> mapByTenantDbName(String dbNamePrefix, Collection<String> tenantIds);

}
