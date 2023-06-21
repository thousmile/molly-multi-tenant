package com.xaaef.molly.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaaef.molly.tenant.base.service.impl.BaseServiceImpl;
import com.xaaef.molly.system.entity.SysMenu;
import com.xaaef.molly.system.mapper.SysMenuMapper;
import com.xaaef.molly.system.service.SysMenuService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.xaaef.molly.common.enums.UserType.SYSTEM;
import static com.xaaef.molly.common.enums.UserType.TENANT;
import static com.xaaef.molly.auth.jwt.JwtSecurityUtils.isMasterUser;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/9 15:36
 */


@Slf4j
@Service
@AllArgsConstructor
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuMapper, SysMenu>
        implements SysMenuService {


    @Override
    public boolean save(SysMenu entity) {
        if (this.exist(SysMenu::getPerms, entity.getPerms())) {
            throw new RuntimeException(StrUtil.format("权限标识: {} 已经存在了", entity.getPerms()));
        }
        return super.save(entity);
    }


    @Override
    public boolean updateById(SysMenu entity) {
        var wrapper = new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getPerms, entity.getPerms())
                .ne(SysMenu::getMenuId, entity.getMenuId());
        if (this.exist(wrapper)) {
            throw new RuntimeException(StrUtil.format("权限标识: {} 已经存在了", entity.getPerms()));
        }
        return super.updateById(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeById(Serializable id) {
        Long mid = Long.valueOf(id.toString());
        if (baseMapper.haveChildren(mid) > 0) {
            throw new RuntimeException("当前菜单有子菜单引用！无法删除！");
        }
        if (baseMapper.templateReference(mid) > 0) {
            throw new RuntimeException("当前菜单有权限模板引用！无法删除！");
        }
        return super.removeById(id);
    }


    @Override
    public List<Tree<Long>> tree() {
        return buildTree(this.list());
    }


    @Override
    public List<Tree<Long>> treeCurrentUserAvailable() {
        var wrapper = new LambdaQueryWrapper<SysMenu>();
        wrapper.select(SysMenu::getMenuId, SysMenu::getParentId, SysMenu::getMenuName, SysMenu::getSort);
        if (isMasterUser()) {
            // 如果是系统用户，那么就获取，非租户的所有菜单
            wrapper.ne(SysMenu::getTarget, TENANT.getCode());
        } else {
            // 如果是租户用户，那么就获取，非系统的所有菜单
            wrapper.ne(SysMenu::getTarget, SYSTEM.getCode());
        }
        var list = baseMapper.selectList(wrapper);
        return buildTree(list);
    }


    @Override
    public List<Tree<Long>> treeTenantAvailable() {
        var wrapper = new LambdaQueryWrapper<SysMenu>();
        wrapper.select(SysMenu::getMenuId, SysMenu::getParentId, SysMenu::getMenuName, SysMenu::getSort);
        wrapper.ne(SysMenu::getTarget, SYSTEM.getCode());
        var list = baseMapper.selectList(wrapper);
        return buildTree(list);
    }


    private List<Tree<Long>> buildTree(List<SysMenu> menus) {
        List<Tree<Long>> roots = new ArrayList<>();
        if (menus != null && !menus.isEmpty()) {
            var all = menus.stream()
                    .map(r -> {
                        var node = new TreeNode<>(r.getMenuId(), r.getParentId(), r.getMenuName(), r.getSort());
                        var targetMap = new HashMap<String, Object>();
                        BeanUtil.beanToMap(r, targetMap, CopyOptions.create().setIgnoreNullValue(true));
                        node.setExtra(targetMap);
                        return node;
                    })
                    .collect(Collectors.toList());
            roots.addAll(TreeUtil.build(all, 0L));
        }
        return roots;
    }


}
