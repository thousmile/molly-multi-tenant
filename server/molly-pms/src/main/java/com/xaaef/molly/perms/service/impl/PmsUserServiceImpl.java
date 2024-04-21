package com.xaaef.molly.perms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xaaef.molly.auth.jwt.JwtLoginUser;
import com.xaaef.molly.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.auth.service.JwtTokenService;
import com.xaaef.molly.auth.service.UserLoginService;
import com.xaaef.molly.common.enums.AdminFlag;
import com.xaaef.molly.common.enums.StatusEnum;
import com.xaaef.molly.common.enums.UserType;
import com.xaaef.molly.common.exception.BizException;
import com.xaaef.molly.common.util.IdUtils;
import com.xaaef.molly.internal.api.ApiCmsProjectService;
import com.xaaef.molly.internal.api.ApiSysConfigService;
import com.xaaef.molly.internal.api.ApiSysMenuService;
import com.xaaef.molly.internal.dto.PmsRoleDTO;
import com.xaaef.molly.internal.dto.SysMenuDTO;
import com.xaaef.molly.perms.entity.PmsDept;
import com.xaaef.molly.perms.entity.PmsRole;
import com.xaaef.molly.perms.entity.PmsUser;
import com.xaaef.molly.perms.mapper.PmsRoleMapper;
import com.xaaef.molly.perms.mapper.PmsUserMapper;
import com.xaaef.molly.perms.po.UserQueryPO;
import com.xaaef.molly.perms.service.PmsDeptService;
import com.xaaef.molly.perms.service.PmsRoleService;
import com.xaaef.molly.perms.service.PmsUserService;
import com.xaaef.molly.perms.vo.*;
import com.xaaef.molly.tenant.base.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.xaaef.molly.auth.jwt.JwtSecurityUtils.*;
import static com.xaaef.molly.common.consts.ConfigDataConst.DEFAULT_USER_PASSWORD;
import static com.xaaef.molly.common.enums.AdminFlag.NO;
import static com.xaaef.molly.common.enums.MenuTypeEnum.BUTTON;
import static com.xaaef.molly.common.enums.MenuTypeEnum.MENU;
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

    private final ApiCmsProjectService cmsProjectService;

    private final ApiSysMenuService menuService;

    private final UserLoginService userLoginService;

    private final PmsRoleService roleService;

    private final PmsDeptService deptService;

    private final PmsRoleMapper roleMapper;

    private final JwtTokenService jwtTokenService;


    @Override
    public IPage<PmsUser> pageKeywords(UserQueryPO params) {
        var wrapper = super.getKeywordsQueryWrapper(params,
                List.of(PmsUser::getUsername, PmsUser::getNickname));
        if (params.getDeptId() != null && params.getDeptId() > 0L) {
            var childIds = deptService.listChildIdByDeptId(params.getDeptId());
            wrapper.lambda().in(PmsUser::getDeptId, childIds);
        }
        Page<PmsUser> pageRequest = Page.of(params.getPageIndex(), params.getPageSize());
        Page<PmsUser> result = super.page(pageRequest, wrapper);
        if (params.isIncludeCauu()) {
            reflectionFill(result.getRecords());
        }
        // 包含 角色和用户信息
        if (params.isIncludeRad()) {
            setRoleAndDept(result.getRecords());
        }
        // 包含 用户在线状态
        var loginUserMap = jwtTokenService.mapLoginUser();
        result.getRecords().forEach(r -> {
            r.setPassword(null);
            // 如果 LoginId 为空。表示未在线！
            var loginUser = loginUserMap.getOrDefault(r.getUserId(), new JwtLoginUser().setLoginId(StrUtil.EMPTY));
            r.setLoginId(loginUser.getLoginId());
        });
        return result;
    }


    private void setRoleAndDept(Collection<PmsUser> list) {
        if (!list.isEmpty()) {
            var userIds = list.stream().map(PmsUser::getUserId).collect(Collectors.toSet());
            var deptIds = list.stream().map(PmsUser::getDeptId).collect(Collectors.toSet());
            var roleMaps = roleService.listByUserIds(userIds);
            var deptMaps = deptService.listByIds(deptIds).stream().collect(
                    Collectors.toMap(PmsDept::getDeptId, d -> d, (o1, o2) -> o2));
            list.forEach(r -> {
                r.setRoles(roleMaps.get(r.getUserId()));
                r.setDept(deptMaps.get(r.getDeptId()));
            });
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(PmsUser entity) {
        if (exist(PmsUser::getUsername, entity.getUsername())) {
            throw new BizException(String.format("用户名 %s 已经存在了！", entity.getUsername()));
        }
        if (exist(PmsUser::getMobile, entity.getMobile())) {
            throw new BizException(String.format("手机号码 %s 已经存在了！", entity.getMobile()));
        }
        if (exist(PmsUser::getEmail, entity.getEmail())) {
            throw new BizException(String.format("邮箱账号 %s 已经存在了！", entity.getEmail()));
        }
        // 如果用户密码为空
        if (StringUtils.isBlank(entity.getPassword())) {
            var userDefaultPassword = Optional.ofNullable(configService.getValueByKey(DEFAULT_USER_PASSWORD.getKey()))
                    .orElse(DEFAULT_USER_PASSWORD.getValue());
            entity.setPassword(userDefaultPassword);
        }

        // 密码加密
        entity.setPassword(encryptPassword(entity.getPassword()));

        if (entity.getAdminFlag() == null || !isAdminUser()) {
            entity.setAdminFlag(NO.getCode());
        }

        if (entity.getStatus() == null) {
            entity.setStatus(StatusEnum.NORMAL.getCode());
        }
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
            throw new BizException(String.format("用户id: %s 的用户不存在！", id));
        }
        // 管理员用户，是无法删除的！
        if (Objects.equals(user.getAdminFlag(), AdminFlag.YES.getCode())) {
            throw new BizException(String.format("用户: %s 是管理员，无法删除！", user.getNickname()));
        }
        // 删除用户的角色
        baseMapper.deleteHaveRoles(user.getUserId());
        return super.removeById(id);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateById(PmsUser entity) {
        var wrapper1 = new LambdaQueryWrapper<PmsUser>()
                .eq(PmsUser::getUsername, entity.getUsername())
                .ne(PmsUser::getUserId, entity.getUserId());
        if (this.count(wrapper1) > 0) {
            throw new BizException(String.format("用户名: %s 已经存在了！", entity.getUsername()));
        }
        var wrapper2 = new LambdaQueryWrapper<PmsUser>()
                .eq(PmsUser::getMobile, entity.getMobile())
                .ne(PmsUser::getUserId, entity.getUserId());
        if (this.count(wrapper2) > 0) {
            throw new BizException(String.format("手机号码: %s 已经存在了！", entity.getMobile()));
        }
        var wrapper3 = new LambdaQueryWrapper<PmsUser>()
                .eq(PmsUser::getEmail, entity.getEmail())
                .ne(PmsUser::getUserId, entity.getUserId());
        if (this.count(wrapper3) > 0) {
            throw new BizException(String.format("邮箱账号: %s 已经存在了！", entity.getEmail()));
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
            // 非管理员 用户。根据部门Id获取关联的项目Id
            if (loginUser.getUserType() == UserType.TENANT
                    && Objects.equals(entity.getAdminFlag(), AdminFlag.NO.getCode())) {
                // 获取当前登录的用户 项目ID 列表
                var haveProjectIds = cmsProjectService.listProjectByDeptId(entity.getDeptId());
                loginUser.setHaveProjectIds(haveProjectIds);
            }
            userLoginService.refreshAuthoritys(loginUser);
        }
        return super.updateById(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateUserRoles(Long userId, Set<Long> roleIds) {
        var dbUser = this.getById(userId);
        if (dbUser == null) {
            throw new BizException(String.format("用户: %d 不存在！", userId));
        }
        // 先删除当前用户拥有的所有角色
        baseMapper.deleteHaveRoles(userId);
        if (roleIds == null || roleIds.isEmpty()) {
            return false;
        }
        // 再赋值新的角色
        return baseMapper.insertByRoles(userId, roleIds) > 0;
    }


    @Override
    public UserRightsVO getUserRights() {
        // 获取 当前登录的用户 所属的租户
        String tenantId = JwtSecurityUtils.getTenantId();
        return delegate(tenantId, () -> {
            // 用户菜单权限
            var userMenus = new HashMap<Long, SysMenuDTO>();
            // 如果是管理员，就获取全部的权限
            if (isAdminUser()) {
                if (isMasterUser()) {
                    // 系统管理员就获取 非租户的全部菜单
                    var menuMap = menuService.listMenuByNonTenant()
                            .stream()
                            .collect(Collectors.toMap(SysMenuDTO::getMenuId, Function.identity(), (oldValue, newValue) -> newValue));
                    userMenus.putAll(menuMap);
                } else {
                    // 租户管理员，就获取租户全部的菜单
                    var menuMap = menuService.listMenuByTenantId(tenantId)
                            .stream()
                            .collect(Collectors.toMap(SysMenuDTO::getMenuId, Function.identity(), (oldValue, newValue) -> newValue));
                    userMenus.putAll(menuMap);
                }
            } else {
                var loginUser = getLoginUser();
                if (loginUser.getRoles() == null || loginUser.getRoles().isEmpty()) {
                    return new UserRightsVO().setButtons(new HashSet<>()).setMenus(new ArrayList<>());
                }
                // 获取当前用户，拥有的 角色ID
                var roleIds = loginUser.getRoles().stream().map(PmsRoleDTO::getRoleId).collect(Collectors.toSet());
                // 根据 角色ID , 获取全部菜单
                var menuIds = roleMapper.selectMenuIdByRoleIds(roleIds);
                if (menuIds.isEmpty()) {
                    return new UserRightsVO().setButtons(new HashSet<>()).setMenus(new ArrayList<>());
                }
                var menuMap = menuService.listMenuByMenuIds(menuIds)
                        .stream()
                        .collect(Collectors.toMap(SysMenuDTO::getMenuId, Function.identity(), (oldValue, newValue) -> newValue));
                userMenus.putAll(menuMap);
            }

            // 获取菜单
            var nodeList = userMenus.values()
                    .stream()
                    .filter(r -> r.getMenuType() == MENU.getCode()).map(r -> {
                        var meta = new MenuMetaVO()
                                .setTitle(r.getMenuName())
                                .setIcon(r.getIcon())
                                .setHidden(r.getVisible() == 0)
                                .setKeepAlive(r.getKeepAlive() == 1);
                        var node = new TreeNode<>(r.getMenuId(), r.getParentId(), r.getPerms(), r.getSort());
                        node.setExtra(
                                Map.of(
                                        "meta", meta,
                                        "component", r.getComponent(),
                                        "path", r.getPath()
                                )
                        );
                        return node;
                    }).collect(Collectors.toList());

            // 将 菜单列表，递归成 树节点的形式
            var treeMenus = TreeUtil.build(nodeList, 0L);

            // 获取所有的按钮
            var buttons = userMenus.values()
                    .stream()
                    .filter(r -> r.getMenuType() == BUTTON.getCode())
                    .map(r -> new ButtonVO().setPerms(r.getPerms()).setTitle(r.getMenuName()))
                    .collect(Collectors.toSet());

            return new UserRightsVO().setMenus(treeMenus).setButtons(buttons);
        });
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updatePassword(UpdatePasswordVO pwd) {
        // 修改密码。要切换到当前登录用户所在的租户
        // 否则，当平台用户在操作其他租户时，选择修改自己的用户名密码。就查不到自己的用户信息
        return delegate(getTenantId(), () -> {
            var sysUser = baseMapper.selectById(pwd.getUserId());
            if (!StringUtils.equals(pwd.getNewPwd(), pwd.getConfirmPwd())) {
                throw new BizException("新密码与确认密码不一致，请重新输入！");
            }
            // 判断 旧的密码是否正确。
            if (matchesPassword(pwd.getOldPwd(), sysUser.getPassword())) {
                // 判断 新密码 和 老密码是否相同
                if (matchesPassword(pwd.getNewPwd(), sysUser.getPassword())) {
                    throw new BizException("新密码与旧密码相同，请重新输入！");
                } else {
                    // 新密码 加密
                    var newPassword = encryptPassword(pwd.getNewPwd());
                    // 修改
                    var pmsUser = PmsUser.builder().userId(pwd.getUserId()).password(newPassword).build();
                    return super.updateById(pmsUser);
                }
            } else {
                throw new BizException("旧密码错误，请重新输入！");
            }
        });
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
        throw new BizException("用户不存在，无法重置密码！");
    }


}
