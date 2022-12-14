package com.xaaef.molly.perms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xaaef.molly.perms.entity.PmsRole;
import com.xaaef.molly.perms.entity.PmsRoleProxy;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Set;


public interface PmsRoleMapper extends BaseMapper<PmsRole> {

    // 角色添加 菜单权限
    int insertByMenus(@Param("roleId") Long roleId,
                      @Param("menus") Set<Long> menus);


    // 删除 角色 的所有权限
    @Delete("delete from pms_user_role where role_id = #{roleId}")
    int deleteHaveMenus(Long roleId);


    // 是否还有 用户 引用此角色
    @Select("select count(*) from pms_user_role where role_id = #{roleId}")
    int userReference(Long roleId);


    /**
     * 查询用户的角色
     *
     * @author WangChenChen
     * @date 2022/3/22 18:14
     */
    Set<PmsRoleProxy> selectListByUserIds(@Param("userIds") Set<Long> userIds);


}