package com.xaaef.molly.system.service.impl;

import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaaef.molly.core.tenant.base.service.impl.BaseServiceImpl;
import com.xaaef.molly.system.entity.SysMenu;
import com.xaaef.molly.system.entity.SysTemplate;
import com.xaaef.molly.system.entity.SysTemplateProxy;
import com.xaaef.molly.system.enums.MenuTargetEnum;
import com.xaaef.molly.system.mapper.SysTemplateMapper;
import com.xaaef.molly.system.service.SysMenuService;
import com.xaaef.molly.system.service.SysTemplateService;
import com.xaaef.molly.system.vo.UpdateMenusVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


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
public class SysTemplateServiceImpl extends BaseServiceImpl<SysTemplateMapper, SysTemplate>
        implements SysTemplateService {

    private final SysMenuService menuService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeById(Serializable id) {
        Long tid = Long.valueOf(id.toString());
        if (baseMapper.userReference(tid) > 0) {
            throw new RuntimeException("此模板，还有租户在使用，无法删除！");
        }
        // 先删除拥有的菜单
        baseMapper.deleteHaveMenus(tid);
        return super.removeById(tid);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(SysTemplate entity) {
        boolean result = super.save(entity);
        // updateMenus(entity.getId(), entity.getMenuIds());
        return result;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateById(SysTemplate entity) {
        boolean result = super.updateById(entity);
        // updateMenus(entity.getId(), entity.getMenuIds());
        return result;
    }


    @Override
    public UpdateMenusVO listHaveMenus(Long templateId) {
        // 当前模板，已经拥有的菜单ID
        var haveHashSet = baseMapper.selectByTemplateId(templateId);

        // 获取 非系统菜单
        var wrapper = new LambdaQueryWrapper<SysMenu>();
        wrapper.select(SysMenu::getMenuId, SysMenu::getParentId, SysMenu::getMenuName, SysMenu::getSort)
                .ne(SysMenu::getTarget, MenuTargetEnum.SYSTEM.getCode());

        var menus = menuService.list(wrapper)
                .stream().map(r -> new TreeNode<>(r.getMenuId(), r.getParentId(), r.getMenuName(), r.getSort()))
                .collect(Collectors.toList());

        var roots = TreeUtil.build(menus, 0L);
        return UpdateMenusVO.builder().have(haveHashSet).all(roots).build();
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateMenus(Long templateId, Set<Long> menus) {
        if (!exist(SysTemplate::getId, templateId)) {
            throw new RuntimeException(String.format("模板ID %d 不存在！", templateId));
        }
        baseMapper.deleteHaveMenus(templateId);
        if (menus != null && menus.size() > 0) {
            return baseMapper.insertByMenus(templateId, menus) > 0;
        }
        return true;
    }


    @Override
    public Map<String, Set<SysTemplate>> listByTenantIds(Set<String> tenantIds) {
        return baseMapper.selectListByTenantIds(tenantIds)
                .stream()
                .collect(Collectors.groupingBy(
                                SysTemplateProxy::getTenantId,
                                Collectors.mapping(r -> {
                                    return SysTemplate.builder()
                                            .id(r.getId())
                                            .name(r.getName())
                                            .description(r.getDescription())
                                            .build();
                                }, Collectors.toSet())
                        )
                );
    }


}
