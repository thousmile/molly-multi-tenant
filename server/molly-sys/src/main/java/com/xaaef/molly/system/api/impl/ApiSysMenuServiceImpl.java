package com.xaaef.molly.system.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaaef.molly.common.enums.MenuTargetEnum;
import com.xaaef.molly.internal.api.ApiSysMenuService;
import com.xaaef.molly.internal.dto.SysMenuDTO;
import com.xaaef.molly.system.entity.SysMenu;
import com.xaaef.molly.system.mapper.SysMenuMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/2/14 11:38
 */


@Slf4j
@Service
@AllArgsConstructor
public class ApiSysMenuServiceImpl implements ApiSysMenuService {

    private final SysMenuMapper menuMapper;


    private Set<SysMenuDTO> listToMenuDTO(List<SysMenu> menus) {
        return menus.stream().map(source -> {
            var target = new SysMenuDTO();
            BeanUtils.copyProperties(source, target);
            return target;
        }).collect(Collectors.toSet());
    }


    @Override
    public Set<String> listPermsByNonTenant() {
        var wrapper = new LambdaQueryWrapper<SysMenu>()
                .select(SysMenu::getPerms)
                .ne(SysMenu::getTarget, MenuTargetEnum.TENANT.getCode());
        return menuMapper.selectList(wrapper)
                .stream()
                .map(SysMenu::getPerms)
                .collect(Collectors.toSet());
    }


    @Override
    public Set<SysMenuDTO> listMenuByNonTenant() {
        var wrapper = new LambdaQueryWrapper<SysMenu>()
                .ne(SysMenu::getTarget, MenuTargetEnum.TENANT.getCode());
        var sysMenus = menuMapper.selectList(wrapper);
        return listToMenuDTO(sysMenus);
    }


    @Override
    public Set<String> listPermsByTenantId(String tenantId) {
        return menuMapper.selectPermsByTenantId(tenantId);
    }


    @Override
    public Set<SysMenuDTO> listMenuByTenantId(String tenantId) {
        var sysMenus = menuMapper.selectByTenantId(tenantId);
        return listToMenuDTO(sysMenus);
    }


    @Override
    public Set<String> listPermsByMenuIds(Set<Long> menuIds) {
        var wrapper = new LambdaQueryWrapper<SysMenu>()
                .select(SysMenu::getPerms)
                .in(SysMenu::getMenuId, menuIds);
        return menuMapper.selectList(wrapper)
                .stream()
                .map(SysMenu::getPerms)
                .collect(Collectors.toSet());
    }


    @Override
    public Set<SysMenuDTO> listMenuByMenuIds(Set<Long> menuIds) {
        return listToMenuDTO(menuMapper.selectBatchIds(menuIds));
    }


}
