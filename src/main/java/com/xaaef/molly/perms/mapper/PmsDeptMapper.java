package com.xaaef.molly.perms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xaaef.molly.perms.entity.PmsDept;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

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
     * 修改子元素关系
     *
     * @param depts 子元素
     */
    int updateChildDept(@Param("depts") List<PmsDept> depts);


}
