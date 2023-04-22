package com.xaaef.molly.perms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.xaaef.molly.auth.jwt.JwtLoginUser;
import com.xaaef.molly.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.util.IdUtils;
import com.xaaef.molly.common.enums.AdminFlag;
import com.xaaef.molly.common.enums.StatusEnum;
import com.xaaef.molly.auth.service.JwtTokenService;
import com.xaaef.molly.internal.api.ApiSysConfigService;
import com.xaaef.molly.internal.api.ApiSysMenuService;
import com.xaaef.molly.internal.dto.PmsRoleDTO;
import com.xaaef.molly.internal.dto.SysMenuDTO;
import com.xaaef.molly.tenant.base.service.impl.BaseServiceImpl;
import com.xaaef.molly.perms.entity.PmsDept;
import com.xaaef.molly.perms.entity.PmsRole;
import com.xaaef.molly.perms.entity.PmsUser;
import com.xaaef.molly.perms.mapper.PmsRoleMapper;
import com.xaaef.molly.perms.mapper.PmsUserMapper;
import com.xaaef.molly.perms.service.PmsDeptService;
import com.xaaef.molly.perms.service.PmsRoleService;
import com.xaaef.molly.perms.service.PmsUserService;
import com.xaaef.molly.perms.vo.*;
import com.xaaef.molly.common.util.TenantUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.xaaef.molly.auth.jwt.JwtSecurityUtils.*;
import static com.xaaef.molly.common.consts.ConfigName.USER_DEFAULT_PASSWORD;
import static com.xaaef.molly.common.enums.AdminFlag.*;
import static com.xaaef.molly.common.enums.MenuTypeEnum.*;
import static com.xaaef.molly.tenant.util.DelegateUtils.delegate;


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
public class PmsUserServiceImpl extends BaseServiceImpl<PmsUserMapper, PmsUser> implements PmsUserService {

    private final ApiSysConfigService configService;

    private final ApiSysMenuService menuService;

    private final PmsRoleService roleService;

    private final PmsDeptService deptService;

    private final PmsRoleMapper roleMapper;

    private final JwtTokenService jwtTokenService;


    @Override
    public IPage<PmsUser> pageKeywords(SearchPO params, SFunction<PmsUser, ?>... columns) {
        var result = super.pageKeywords(params, columns);
        setRoleAndDept(result.getRecords());
        return result;
    }


    private void setRoleAndDept(Collection<PmsUser> list) {
        if (!list.isEmpty()) {
            var userIds = list.stream().map(PmsUser::getUserId).collect(Collectors.toSet());
            var deptIds = list.stream().map(PmsUser::getDeptId).collect(Collectors.toSet());
            var roleMaps = roleService.listByUserIds(userIds);
            var deptMaps = deptService.listByIds(deptIds).stream().collect(Collectors.toMap(PmsDept::getDeptId, d -> d));
            var online = jwtTokenService.listUsernames();
            list.forEach(r -> {
                r.setPassword(null);
                r.setRoles(roleMaps.get(r.getUserId()));
                r.setDept(deptMaps.get(r.getDeptId()));
                // 如果包含，那么就是在线。【 0.离线  1.在线】
                r.setLoginFlag(CollectionUtil.contains(online, r.getUsername()) ? (byte) 1 : (byte) 0);
            });
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(PmsUser entity) {
        if (exist(PmsUser::getUsername, entity.getUsername())) {
            throw new RuntimeException(String.format("用户名 %s 已经存在了！", entity.getUsername()));
        }
        if (exist(PmsUser::getMobile, entity.getMobile())) {
            throw new RuntimeException(String.format("手机号码 %s 已经存在了！", entity.getMobile()));
        }
        if (exist(PmsUser::getEmail, entity.getEmail())) {
            throw new RuntimeException(String.format("邮箱账号 %s 已经存在了！", entity.getEmail()));
        }
        // 如果用户密码为空
        if (StringUtils.isBlank(entity.getPassword())) {
            var userDefaultPassword = Optional.ofNullable(configService.getValueByKey(USER_DEFAULT_PASSWORD)).orElse("123456");
            entity.setPassword(userDefaultPassword);
        }

        // 密码加密
        entity.setPassword(encryptPassword(entity.getPassword()));

        if (entity.getAdminFlag() == null) {
            entity.setAdminFlag(NO.getCode());
        } else {
            if (!isAdminUser()) {
                entity.setAdminFlag(NO.getCode());
            }
        }

        entity.setStatus(StatusEnum.NORMAL.getCode());
        entity.setUserId(IdUtils.getStandaloneId());
        var ok = super.save(entity);

        if (entity.getRoles() != null) {
            // 角色列表
            var roleIds = entity.getRoles().stream().map(PmsRole::getRoleId).collect(Collectors.toSet());
            // 修改 角色
            updateUserRoles(entity.getUserId(), roleIds);
        }

        return ok;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeById(Serializable id) {
        var user = this.getById(id);
        if (user == null) {
            throw new RuntimeException(String.format("用户id为 [ %s ] 的用户不存在！", id));
        }
        // 管理员用户，是无法删除的！
        if (Objects.equals(user.getAdminFlag(), AdminFlag.YES.getCode())) {
            throw new RuntimeException(String.format("用户[ %s ] 是管理员，无法删除！", user.getNickname()));
        }
        // 删除用户的角色
        baseMapper.deleteHaveRoles(user.getUserId());
        return super.removeById(id);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateById(PmsUser entity) {
        var wrapper1 = new LambdaQueryWrapper<PmsUser>().eq(PmsUser::getUsername, entity.getUsername()).ne(PmsUser::getUserId, entity.getUserId());
        if (this.count(wrapper1) > 0) {
            throw new RuntimeException(String.format("用户名 %s 已经存在了！", entity.getUsername()));
        }
        var wrapper2 = new LambdaQueryWrapper<PmsUser>().eq(PmsUser::getMobile, entity.getMobile()).ne(PmsUser::getUserId, entity.getUserId());
        if (this.count(wrapper2) > 0) {
            throw new RuntimeException(String.format("手机号码 %s 已经存在了！", entity.getMobile()));
        }
        var wrapper3 = new LambdaQueryWrapper<PmsUser>().eq(PmsUser::getEmail, entity.getEmail()).ne(PmsUser::getUserId, entity.getUserId());
        if (this.count(wrapper3) > 0) {
            throw new RuntimeException(String.format("邮箱账号 %s 已经存在了！", entity.getEmail()));
        }
        if (entity.getRoles() != null) {
            // 角色列表
            var roleIds = entity.getRoles().stream().map(PmsRole::getRoleId).collect(Collectors.toSet());
            // 修改 原有角色
            updateUserRoles(entity.getUserId(), roleIds);
        }
        entity.setPassword(null);
        // 根据用户名，获取 登录的用户信息
        var loginUser = jwtTokenService.getLoginUserByUsername(entity.getUsername());
        if (loginUser != null) {
            var copyOptions = CopyOptions.create();
            copyOptions.setIgnoreNullValue(true);
            BeanUtil.copyProperties(entity, loginUser, copyOptions);
            jwtTokenService.updateLoginUser(loginUser);
        }
        return super.updateById(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateUserRoles(Long userId, Set<Long> roleIds) {
        var dbUser = this.getById(userId);
        if (dbUser == null) {
            throw new RuntimeException(String.format("用户 %d 不存在！", userId));
        }
        // 先删除当前用户拥有的所有角色
        baseMapper.deleteHaveRoles(userId);
        if (roleIds == null || roleIds.isEmpty()) {
            return 1;
        }
        // 再赋值新的角色
        return baseMapper.insertByRoles(userId, roleIds);
    }


    @Override
    public UserRightsVO getUserRights() {
        // 获取 当前登录的用户 所属的租户
        String tenantId = JwtSecurityUtils.getTenantId();
        return delegate(tenantId, () -> {
            // 用户菜单权限
            Set<SysMenuDTO> userMenus = null;
            // 如果是管理员，就获取全部的权限
            if (isAdminUser()) {
                if (isMasterUser()) {
                    // 系统管理员就获取 非租户的全部菜单
                    userMenus = menuService.listMenuByNonTenant();
                } else {
                    // 租户管理员，就获取租户全部的菜单
                    userMenus = menuService.listMenuByTenantId(tenantId);
                }
            } else {
                // 获取当前用户，拥有的 角色ID
                var roleIds = getLoginUser().getRoles().stream().map(PmsRoleDTO::getRoleId).collect(Collectors.toSet());
                if (roleIds.isEmpty()) {
                    return new UserRightsVO().setButtons(Set.of()).setMenus(List.of());
                }
                // 根据 角色ID , 获取全部菜单
                var menuIds = roleMapper.selectMenuIdByRoleIds(roleIds);
                if (menuIds.isEmpty()) {
                    return new UserRightsVO().setButtons(Set.of()).setMenus(List.of());
                }
                userMenus = menuService.listMenuByMenuIds(menuIds);
            }

            // 获取菜单
            var nodeList = userMenus.stream().distinct().filter(r -> r.getMenuType() == MENU.getCode()).map(r -> {
                var meta = new MenuMetaVO().setTitle(r.getMenuName()).setIcon(r.getIcon()).setHidden(r.getVisible() == 0);
                var node = new TreeNode<>(r.getMenuId(), r.getParentId(), r.getPerms(), r.getSort());
                node.setExtra(Map.of("meta", meta, "component", r.getComponent(), "path", r.getPath()));
                return node;
            }).collect(Collectors.toList());

            // 将 菜单列表，递归成 树节点的形式
            var treeMenus = TreeUtil.build(nodeList, 0L);

            // 获取所有的按钮
            var buttons = userMenus.stream().distinct().filter(r -> r.getMenuType() == BUTTON.getCode()).map(r -> new ButtonVO().setPerms(r.getPerms()).setTitle(r.getMenuName())).collect(Collectors.toSet());

            return new UserRightsVO().setMenus(treeMenus).setButtons(buttons);
        });
    }


    @Override
    public Set<JwtLoginUser> listLoginUsers() {
        return jwtTokenService.listLoginUsers()
                .stream()
                .peek(r -> {
                    r.setAuthorities(null);
                }).collect(Collectors.toSet());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updatePassword(UpdatePasswordVO pwd) {
        var sysUser = baseMapper.selectById(pwd.getUserId());
        if (!StringUtils.equals(pwd.getNewPwd(), pwd.getConfirmPwd())) {
            throw new RuntimeException("新密码与确认密码不一致，请重新输入！");
        }
        // 判断 老密码是否正确。
        if (matchesPassword(pwd.getOldPwd(), sysUser.getPassword())) {
            // 判断 新密码 和 老密码是否相同
            if (matchesPassword(pwd.getNewPwd(), sysUser.getPassword())) {
                throw new RuntimeException("新密码与旧密码相同，请重新输入！");
            } else {
                // 新密码 加密
                var newPassword = encryptPassword(pwd.getNewPwd());
                // 修改
                var pmsUser = PmsUser.builder().userId(pwd.getUserId()).password(newPassword).build();
                return super.updateById(pmsUser);
            }
        }
        throw new RuntimeException("旧密码错误，请重新输入！");
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean resetPassword(ResetPasswordVO pwd) {
        var sysUser = baseMapper.selectById(pwd.getUserId());
        if (sysUser != null) {
            // 新密码 加密
            String newPassword = encryptPassword(pwd.getNewPwd());
            // 修改
            var pmsUser = new PmsUser().setUserId(pwd.getUserId()).setPassword(newPassword);
            return super.updateById(pmsUser);
        }
        throw new RuntimeException("用户不存在，无法重置密码！");
    }


}
