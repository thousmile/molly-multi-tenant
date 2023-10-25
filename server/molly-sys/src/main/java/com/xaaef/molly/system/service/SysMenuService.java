package com.xaaef.molly.system.service;

import cn.hutool.core.lang.tree.Tree;
import com.xaaef.molly.system.entity.SysMenu;
import com.xaaef.molly.tenant.base.service.BaseService;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/9 15:33
 */

public interface SysMenuService extends BaseService<SysMenu> {

    /**
     * 获取所有菜单 。以树节点展示
     */
    List<Tree<Long>> tree();


    /**
     * 获取当前用户可用的所有菜单
     */
    List<Tree<Long>> treeCurrentUserAvailable();


    /**
     * 获取 租户可用的所有菜单
     */
    List<Tree<Long>> treeTenantAvailable();


}
