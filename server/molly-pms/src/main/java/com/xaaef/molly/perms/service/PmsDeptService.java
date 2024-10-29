package com.xaaef.molly.perms.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xaaef.molly.common.po.SearchParentPO;
import com.xaaef.molly.perms.entity.PmsDept;
import com.xaaef.molly.tenant.base.service.BaseService;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/9 15:33
 */

public interface PmsDeptService extends BaseService<PmsDept> {

    /**
     * 分页查询
     */
    IPage<PmsDept> pageKeywords(SearchParentPO params);


    /**
     * 获取所有部门，树节点展示
     */
    List<Tree<Long>> treeNode();


    /**
     * 获取用户，所在部门的数据权限
     */
    Set<Long> listHaveDeptIds(Long userId);


    /**
     * 获取 子部门ID
     */
    Set<Long> listChildIdByDeptId(Long deptId);


    /**
     * 获取 子部门信息
     */
    Set<PmsDept> listChildByDeptId(Long deptId);


    /**
     * 根据 部门ID 查询 部门的全名称
     * 如: Google中国/研发部/web前端组
     */
    Map<Long, String> mapDeptFullName(Set<Long> deptIds);


    /**
     * 根据 部门ID 查询 部门的全名称
     * 如: Google中国/研发部/web前端组
     */
    List<PmsDept> listDeptFullName(Set<Long> deptIds);


    /**
     * 根据 用户id 查询 部门列表
     */
    List<PmsDept> listChildDeptByUserId(Long userId);


    /**
     * 根据 用户id 查询 部门id列表
     */
    Set<Long> listChildDeptIdByUserId(Long userId);

}
