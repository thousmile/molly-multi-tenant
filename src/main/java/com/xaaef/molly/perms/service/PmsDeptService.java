package com.xaaef.molly.perms.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.po.SearchParentPO;
import com.xaaef.molly.core.tenant.base.service.BaseService;
import com.xaaef.molly.perms.entity.PmsDept;

import java.util.List;
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


    IPage<PmsDept> pageKeywords(SearchParentPO po);


    List<Tree<Long>> treeNode();


    /**
     * 获取用户，所在部门的数据权限
     */
    Set<Long> listHaveDeptIds(Long userId);


    /**
     * 获取 子部门
     */
    Set<Long> listChildDeptIds(Long deptId);


}
