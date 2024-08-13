package com.xaaef.molly.perms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xaaef.molly.perms.entity.PmsDept;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/14 10:03
 */

public interface PmsDeptMapper extends BaseMapper<PmsDept> {

    // 根据 部门ID，查询 子孙 部门。不包含自己
    @Select("select * from pms_dept where find_in_set(#{deptId}, ancestors)")
    Set<PmsDept> selectChildDept(Long deptId);


    // 根据 部门ID，查询 子孙 部门id。不包含自己
    @Select("select dept_id from pms_dept where find_in_set(#{deptId}, ancestors)")
    Set<Long> selectChildDeptId(Long deptId);


    /**
     * 修改子元素关系
     *
     * @param childs 子元素
     */
    int updateChilds(@Param("childs") Set<PmsDept> childs);


    /**
     * 删除 部门 的所有权限
     *
     * @param id 部门ID
     */
    @Delete("DELETE FROM pms_dept_menu WHERE dept_id = #{id}")
    int deleteHaveMenus(Long id);


    /**
     * 部门 添加 菜单权限
     *
     * @param id    部门ID
     * @param menus 菜单ID
     */
    int insertByMenus(@Param("id") Long id,
                      @Param("menus") Set<Long> menus);


    /**
     * 根据 用户id 查询 部门列表
     */
    List<PmsDept> selectChildDeptByUserId(Long userId);


    /**
     * 根据 用户id 查询 部门id列表
     */
    Set<Long> selectChildDeptIdByUserId(Long userId);


    /**
     * 根据 用户id 查询 部门
     */
    PmsDept selectDeptByUserId(Long userId);


    /**
     * 查询 角色id 部门权限
     */
    List<PmsDept> selectDeptByRuleIds(Set<Long> roleIds);


    /**
     * 查询 角色 部门权限
     */
    Set<Long> selectDeptIdByRuleIds(Set<Long> roleIds);


}
