package com.xaaef.molly.perms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xaaef.molly.common.consts.DataScopeConst;
import com.xaaef.molly.common.domain.LinkedTarget;
import com.xaaef.molly.common.exception.BizException;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.util.TenantUtils;
import com.xaaef.molly.internal.api.ApiSysMenuService;
import com.xaaef.molly.internal.dto.SysMenuDTO;
import com.xaaef.molly.perms.entity.PmsDept;
import com.xaaef.molly.perms.entity.PmsRole;
import com.xaaef.molly.perms.entity.PmsRoleProxy;
import com.xaaef.molly.perms.mapper.PmsDeptMapper;
import com.xaaef.molly.perms.mapper.PmsRoleMapper;
import com.xaaef.molly.perms.service.PmsRoleService;
import com.xaaef.molly.perms.vo.UpdateMenusVO;
import com.xaaef.molly.tenant.base.service.impl.BaseServiceImpl;
import com.xaaef.molly.tenant.service.MultiTenantManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
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
public class PmsRoleServiceImpl extends BaseServiceImpl<PmsRoleMapper, PmsRole> implements PmsRoleService {

    private final ApiSysMenuService menuService;

    private final PmsDeptMapper deptMapper;

    private final MultiTenantManager tenantManager;


    @Override
    public IPage<PmsRole> pageKeywords(SearchPO params) {
        var result = super.pageKeywords(
                params, List.of(PmsRole::getRoleName, PmsRole::getDescription)
        );
        includeDept(result.getRecords());
        return result;
    }


    @Override
    public UpdateMenusVO listHaveDepts(Long roleId) {
        var result = new UpdateMenusVO()
                .setAll(new ArrayList<>())
                .setHave(new HashSet<>());
        var w1 = new LambdaQueryWrapper<PmsDept>()
                .select(List.of(PmsDept::getDeptId, PmsDept::getDeptName,
                        PmsDept::getParentId, PmsDept::getSort));
        final var deptList = deptMapper.selectList(w1);
        if (!deptList.isEmpty()) {
            // 获取全部的菜单
            var all = deptList.stream()
                    .map(r -> new TreeNode<>(
                            r.getDeptId(), r.getParentId(),
                            r.getDeptName(), r.getSort())
                    )
                    .collect(Collectors.toList());
            result.setAll(TreeUtil.build(all, 0L));
        }
        if (roleId > 0) {
            final var haveList = baseMapper.selectDeptIdByRoleIds(Set.of(roleId));
            if (!haveList.isEmpty()) {
                var haveDeptIds = haveList.stream().map(LinkedTarget::getTargetId).collect(Collectors.toSet());
                result.setHave(haveDeptIds);
            }
        }
        return result;
    }


    private void includeDept(Collection<PmsRole> list) {
        if (CollectionUtil.isNotEmpty(list)) {
            var roleIds = list
                    .stream()
                    .filter(r -> Objects.equals(r.getDataScope(), DataScopeConst.CUSTOM))
                    .map(PmsRole::getRoleId)
                    .collect(Collectors.toSet());
            if (!roleIds.isEmpty()) {
                var deptMaps = baseMapper.selectDeptIdByRoleIds(roleIds)
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        LinkedTarget::getSourceId,
                                        Collectors.mapping(LinkedTarget::getTargetId, Collectors.toSet()
                                        )
                                )
                        );
                list.forEach(r -> {
                    var deptIds = deptMaps.getOrDefault(r.getRoleId(), new HashSet<>());
                    r.setDeptIds(deptIds);
                });
            }
        }
    }


    @Override
    public UpdateMenusVO listHaveMenus(Long roleId) {
        var result = new UpdateMenusVO()
                .setAll(new ArrayList<>())
                .setHave(new HashSet<>());
        if (roleId > 0) {
            // 当前角色，已经拥有的菜单ID
            var haveHashSet = baseMapper.selectMenuIdByRoleId(roleId);
            result.setHave(haveHashSet);
        }
        Set<SysMenuDTO> menus = null;

        // 判断当前是否为，默认租户
        boolean defaultTenantId = tenantManager.isDefaultTenantId(TenantUtils.getTenantId());

        if (!defaultTenantId) {
            // 如果是租户用户，那么就获取，当前角色所在租户的，所有菜单
            menus = menuService.listMenuByTenantId(TenantUtils.getTenantId());
        } else {
            // 如果是系统用户，那么就获取，非租户的所有菜单
            menus = menuService.listMenuByNonTenant();
        }

        if (!menus.isEmpty()) {
            // 获取全部的菜单
            var all = menus.stream()
                    .map(r -> new TreeNode<>(
                            r.getMenuId(), r.getParentId(),
                            r.getMenuName(), r.getSort())
                    )
                    .collect(Collectors.toList());
            result.setAll(TreeUtil.build(all, 0L));
        }

        return result;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateMenus(Long roleId, Set<Long> menus) {
        var dbRole = getById(roleId);
        if (dbRole == null) {
            throw new BizException("角色不存在！");
        }
        baseMapper.deleteHaveMenus(roleId);
        if (menus != null && !menus.isEmpty()) {
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
                    .map(source -> BeanUtil.copyProperties(source, PmsRole.class))
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
            throw new BizException(String.format("角色Id [ %d ] 还有其他用户关联！无法删除！", roleId));
        }
        // 删除当前角色，关联的权限
        baseMapper.deleteHaveMenus(roleId);
        // 删除当前角色，关联的部门权限
        baseMapper.deleteHaveDepts(roleId);
        return super.removeById(id);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(PmsRole entity) {
        if (entity.getDataScope() == 2) {
            if (CollectionUtil.isEmpty(entity.getDeptIds())) {
                throw new RuntimeException("自定义数据权限，最少需要选择一个部门");
            }
            var b1 = super.save(entity);
            var b2 = baseMapper.insertByDepts(entity.getRoleId(), entity.getDeptIds()) > 0;
            return b1 && b2;
        }
        return super.save(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateById(PmsRole entity) {
        if (entity.getDataScope() == 2) {
            if (CollectionUtil.isEmpty(entity.getDeptIds())) {
                throw new RuntimeException("自定义数据权限，最少需要选择一个部门");
            }
            baseMapper.deleteHaveDepts(entity.getRoleId());
            var b1 = super.updateById(entity);
            var b2 = baseMapper.insertByDepts(entity.getRoleId(), entity.getDeptIds()) > 0;
            return b1 && b2;
        }
        baseMapper.deleteHaveDepts(entity.getRoleId());
        return super.updateById(entity);
    }


}
