package com.xaaef.molly.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xaaef.molly.system.entity.SysMenu;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;


public interface SysMenuMapper extends BaseMapper<SysMenu> {


    // 查询租户所有权限
    List<SysMenu> selectByTenantId(String tenantId);


    // 查询租户所有权限
    Set<String> selectPermsByTenantId(String tenantId);


    // 判断是否某个权限下，是否还有拥有子权限
    @Select("SELECT COUNT(*) FROM sys_menu WHERE parent_id = #{id}")
    int haveChildren(Long id);


    // 判断是否某个权限，是否还被其他 租户权限模板 引用
    @Select("SELECT COUNT(*) FROM sys_template_menu WHERE menu_id = #{id}")
    int templateReference(Long id);


}