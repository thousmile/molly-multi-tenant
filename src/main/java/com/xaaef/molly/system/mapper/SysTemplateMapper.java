package com.xaaef.molly.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xaaef.molly.system.entity.SysTemplate;
import com.xaaef.molly.system.entity.SysTemplateProxy;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;


public interface SysTemplateMapper extends BaseMapper<SysTemplate> {

    // 模板 添加 菜单权限
    int insertByMenus(@Param("id") Long id, @Param("menus") Set<Long> menus);


    // 是否还有 租户 引用此 模板
    @Select("select count(*) from sys_tenant_template where template_id = #{templateId}")
    int userReference(Long templateId);


    // 删除 模板 的所有菜单
    @Delete("delete from sys_template_menu where template_id = #{templateId}")
    int deleteHaveMenus(Long templateId);


    @Select("select menu_id from sys_template_menu where template_id = #{templateId}")
    Set<Long> selectByTemplateId(Long templateId);


    // 根据 租户id 获取关联的模板
    List<SysTemplateProxy> selectListByTenantIds(@Param("tenantIds") Set<String> tenantIds);

}