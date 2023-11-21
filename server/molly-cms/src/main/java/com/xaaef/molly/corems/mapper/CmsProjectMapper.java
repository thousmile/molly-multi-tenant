package com.xaaef.molly.corems.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xaaef.molly.corems.entity.CmsProject;
import com.xaaef.molly.corems.entity.TenantAndProject;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
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


    // 查询 数据库 所有的 表名称
    @Select("SELECT DISTINCT TABLE_NAME FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = (SELECT DATABASE ())")
    Set<String> selectListTableNames();


    // 查询 所有的 包含 project_id 的表
    @Select("SELECT DISTINCT TABLE_NAME FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = (SELECT DATABASE ()) AND COLUMN_NAME = #{column}")
    Set<String> selectListTableNamesByIncludeColumn(String column);


    // 根据 租户的数据名称  查询 项目列表。如: molly_master 、molly_google
    Set<TenantAndProject> selectListByTenantDbName(Collection<String> tenantDbNameList);


}
