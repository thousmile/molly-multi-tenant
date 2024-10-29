package com.xaaef.molly.corems.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xaaef.molly.corems.entity.CmsProject;
import com.xaaef.molly.corems.entity.TenantAndProject;
import com.xaaef.molly.tenant.ds.DataScope;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 项目
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/14 10:03
 */


public interface CmsProjectMapper extends BaseMapper<CmsProject> {


    // 查询 所有的 不包含 project_id 的表
    @InterceptorIgnore(tenantLine = "true")
    Set<String> selectListTableNamesByNotIncludeColumn(String column);


    // 查询 所有的包含 project_id 的表
    @InterceptorIgnore(tenantLine = "true")
    Set<String> selectListTableNamesByIncludeColumn(String column);


    // 删除项目
    @InterceptorIgnore(tenantLine = "true")
    int deleteByProjectId(@Param("tableName") Set<String> tableName, @Param("projectId") Long projectId);


    // 根据 租户的数据名称  查询 项目列表。如: molly_master 、molly_google
    Set<TenantAndProject> selectListByTenantDbName(Collection<String> tenantDbNameList);


    /**
     * 根据条件分页查询数据
     */
    @DataScope()
    List<CmsProject> selectProjectList(@Param("p") CmsProject user);


    /**
     * 根据条件分页查询数据
     */
    @DataScope()
    IPage<CmsProject> selectProjectPage(IPage<?> page, @Param("p") CmsProject user);


    /**
     * 根据条件分页查询数据
     */
    @DataScope()
    IPage<CmsProject> selectSimpleProjectPage(IPage<?> page, @Param("p") CmsProject user);

}
