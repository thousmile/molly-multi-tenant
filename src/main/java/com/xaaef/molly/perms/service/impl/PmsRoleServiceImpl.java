package com.xaaef.molly.perms.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaaef.molly.core.tenant.base.service.impl.BaseServiceImpl;
import com.xaaef.molly.core.tenant.util.TenantUtils;
import com.xaaef.molly.perms.entity.PmsRole;
import com.xaaef.molly.perms.entity.PmsRoleProxy;
import com.xaaef.molly.perms.mapper.PmsRoleMapper;
import com.xaaef.molly.perms.service.PmsRoleService;
import com.xaaef.molly.system.entity.SysMenu;
import com.xaaef.molly.system.enums.MenuTargetEnum;
import com.xaaef.molly.system.mapper.SysMenuMapper;
import com.xaaef.molly.system.service.SysConfigService;
import com.xaaef.molly.system.vo.UpdateMenusVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.xaaef.molly.core.auth.jwt.JwtSecurityUtils.isMasterUser;


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
public class PmsRoleServiceImpl extends BaseServiceImpl<PmsRoleMapper, PmsRole> implements PmsRoleService {

    private final SysConfigService configService;

    private final SysMenuMapper menuMapper;

    @Override
    public UpdateMenusVO listHaveMenus(Long roleId) {
        PmsRole dbRole = getById(roleId);
        if (dbRole == null) {
            throw new RuntimeException(String.format("角色ID %s 不存在！", roleId));
        }
        // 当前角色，已经拥有的菜单ID
        var haveHashSet = baseMapper.selectMenuIdByRoleId(roleId);

        List<SysMenu> menus = null;

        if (!isMasterUser()) {
            // 如果是租户用户，那么就获取，当前角色所在租户的，所有菜单
            menus = menuMapper.selectByTenantId(TenantUtils.getTenantId());
        } else {
            // 如果是系统用户，那么就获取，非租户的所有菜单
            menus = menuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                    .ne(SysMenu::getTarget, MenuTargetEnum.TENANT.getCode()));
        }
        List<Tree<Long>> roots = new ArrayList<>();
        if (!menus.isEmpty()) {
            // 获取全部的菜单
            var all = menus.stream()
                    .map(r -> new TreeNode<>(r.getMenuId(), r.getParentId(), r.getMenuName(), r.getSort()))
                    .collect(Collectors.toList());
            roots.addAll(TreeUtil.build(all, 0L));
        }
        return UpdateMenusVO.builder().have(haveHashSet).all(roots).build();
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateMenus(Long roleId, Set<Long> menus) {
        var dbRole = getById(roleId);
        if (dbRole == null) {
            throw new RuntimeException("角色不存在！");
        }
        baseMapper.deleteHaveMenus(roleId);
        if (menus != null && menus.size() > 0) {
            return baseMapper.insertByMenus(roleId, menus) > 0;
        }
        return true;
    }


    @Override
    public Map<Long, Set<PmsRole>> listByUserIds(Set<Long> userIds) {
        Map<Long, List<PmsRoleProxy>> collect = baseMapper.selectListByUserIds(userIds).stream()
                .collect(Collectors.groupingBy(PmsRoleProxy::getUserId));
        var longSetMap = new HashMap<Long, Set<PmsRole>>();
        collect.forEach((key, values) -> {
            var roles = values.stream()
                    .map(r -> new PmsRole(r.getRoleId(), r.getRoleName(), null, r.getDescription()))
                    .collect(Collectors.toSet());
            longSetMap.put(key, roles);
        });
        return longSetMap;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeById(Serializable id) {
        Long roleId = Long.valueOf(id.toString());
        if (baseMapper.userReference(roleId) > 0) {
            throw new RuntimeException(String.format("角色Id [ %d ] 还有其他用户关联！无法删除！", roleId));
        }
        // 删除当前角色，关联的权限
        baseMapper.deleteHaveMenus(roleId);
        return super.removeById(id);
    }


}
