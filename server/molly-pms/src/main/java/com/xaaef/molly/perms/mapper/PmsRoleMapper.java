package com.xaaef.molly.perms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xaaef.molly.common.domain.LinkedTarget;
import com.xaaef.molly.perms.entity.PmsRole;
import com.xaaef.molly.perms.entity.PmsRoleProxy;
import com.xaaef.molly.tenant.ds.DataScope;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;


public interface PmsRoleMapper extends BaseMapper<PmsRole> {

    // 角色添加 菜单权限
    int insertByMenus(@Param("roleId") Long roleId,
                      @Param("menus") Set<Long> menus);


    // 删除 角色 的所有权限
    @Delete("delete from pms_role_menu where role_id = #{roleId}")
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

    /**
     * 查询用户的角色
     *
     * @author WangChenChen
     * @date 2022/3/22 18:14
     */
    Set<PmsRoleProxy> selectListByUserId(@Param("userId") Long userId);


    /**
     * 查询 角色 的 菜单
     */
    @Select("select menu_id from pms_role_menu where role_id = #{roleId}")
    Set<Long> selectMenuIdByRoleId(Long roleId);


    /**
     * 查询 角色 的 菜单
     */
    Set<Long> selectMenuIdByRoleIds(Set<Long> roleIds);


    /**
     * 根据 角色 查询关联的部门id
     */
    List<LinkedTarget> selectDeptIdByRoleIds(Set<Long> roleIds);

    /**
     * 删除 角色 关联 的 部门
     */
    @Delete("DELETE FROM pms_role_dept WHERE role_id = #{roleId}")
    int deleteHaveDepts(Long roleId);


    /**
     * 角色 关联 部门
     */
    int insertByDepts(@Param("roleId") Long roleId,
                      @Param("deptIds") Set<Long> deptIds);



    /**
     * 根据条件列表查询数据
     */
    @DataScope()
    List<PmsRole> selectRoleList(@Param("p") PmsRole role);


    /**
     * 根据条件分页查询数据
     */
    @DataScope()
    IPage<PmsRole> selectRolePage(IPage<?> page, @Param("p") PmsRole role);



}