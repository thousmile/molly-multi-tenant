package com.xaaef.molly.perms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
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
            var deptMaps = deptService.listByIds(deptIds)
                    .stream()
                    .collect(Collectors.toMap(PmsDept::getDeptId, d -> d));
            list.forEach(r -> {
                r.setPassword(null);
                r.setRoles(roleMaps.get(r.getUserId()));
                r.setDept(deptMaps.get(r.getDeptId()));
            });
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(PmsUser entity) {
        if (exist(PmsUser::getUsername, entity.getUsername())) {
            throw new RuntimeException(String.format("????????? %s ??????????????????", entity.getUsername()));
        }
        if (exist(PmsUser::getMobile, entity.getMobile())) {
            throw new RuntimeException(String.format("???????????? %s ??????????????????", entity.getMobile()));
        }
        if (exist(PmsUser::getEmail, entity.getEmail())) {
            throw new RuntimeException(String.format("???????????? %s ??????????????????", entity.getEmail()));
        }
        // ????????????????????????
        if (StringUtils.isBlank(entity.getPassword())) {
            var userDefaultPassword = Optional.ofNullable(configService.getValueByKey(USER_DEFAULT_PASSWORD))
                    .orElse("123456");
            entity.setPassword(userDefaultPassword);
        }

        // ????????????
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
            // ????????????
            var roleIds = entity.getRoles()
                    .stream()
                    .map(PmsRole::getRoleId)
                    .collect(Collectors.toSet());
            // ?????? ??????
            updateUserRoles(entity.getUserId(), roleIds);
        }

        return ok;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeById(Serializable id) {
        var user = this.getById(id);
        if (user == null) {
            throw new RuntimeException(String.format("??????id??? [ %s ] ?????????????????????", id));
        }
        // ???????????????????????????????????????
        if (Objects.equals(user.getAdminFlag(), AdminFlag.YES.getCode())) {
            throw new RuntimeException(String.format("??????[ %s ] ??????????????????????????????", user.getNickname()));
        }
        // ?????????????????????
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
            throw new RuntimeException(String.format("????????? %s ??????????????????", entity.getUsername()));
        }
        var wrapper2 = new LambdaQueryWrapper<PmsUser>()
                .eq(PmsUser::getMobile, entity.getMobile())
                .ne(PmsUser::getUserId, entity.getUserId());
        if (this.count(wrapper2) > 0) {
            throw new RuntimeException(String.format("???????????? %s ??????????????????", entity.getMobile()));
        }
        var wrapper3 = new LambdaQueryWrapper<PmsUser>()
                .eq(PmsUser::getEmail, entity.getEmail())
                .ne(PmsUser::getUserId, entity.getUserId());
        if (this.count(wrapper3) > 0) {
            throw new RuntimeException(String.format("???????????? %s ??????????????????", entity.getEmail()));
        }
        if (entity.getRoles() != null) {
            // ????????????
            var roleIds = entity.getRoles()
                    .stream()
                    .map(PmsRole::getRoleId)
                    .collect(Collectors.toSet());
            // ?????? ????????????
            updateUserRoles(entity.getUserId(), roleIds);
        }
        entity.setPassword(null);
        // ??????????????????????????????
        if (Objects.equals(getUserId(), entity.getUserId())) {
            var loginUser = getLoginUser();
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
            throw new RuntimeException(String.format("?????? %d ????????????", userId));
        }
        // ??????????????????????????????????????????
        baseMapper.deleteHaveRoles(userId);
        if (roleIds == null || roleIds.isEmpty()) {
            return 1;
        }
        // ?????????????????????
        return baseMapper.insertByRoles(userId, roleIds);
    }


    @Override
    public UserRightsVO getUserRights() {
        // ??????????????????
        Set<SysMenuDTO> userMenus = null;
        // ?????????????????????????????????????????????
        if (isAdminUser()) {
            if (isMasterUser()) {
                // ???????????????????????? ????????????????????????
                userMenus = menuService.listMenuByNonTenant();
            } else {
                // ????????????????????????????????????????????????
                userMenus = menuService.listMenuByTenantId(getTenantId());
            }
        } else {
            // ?????????????????????????????? ??????ID
            var roleIds = getLoginUser().getRoles()
                    .stream()
                    .map(PmsRoleDTO::getRoleId)
                    .collect(Collectors.toSet());
            if (roleIds.isEmpty()) {
                return UserRightsVO.builder().build();
            }
            // ?????? ??????ID , ??????????????????
            var menuIds = roleMapper.selectMenuIdByRoleIds(roleIds);
            if (menuIds.isEmpty()) {
                return UserRightsVO.builder().build();
            }
            userMenus = menuService.listMenuByMenuIds(menuIds);
        }

        // ????????????
        var nodeList = userMenus.stream()
                .distinct()
                .filter(r -> r.getMenuType() == MENU.getCode())
                .map(r -> {
                    var meta = new MenuMetaVO()
                            .setTitle(r.getMenuName())
                            .setIcon(r.getIcon())
                            .setHidden(r.getVisible() == 0);
                    var node = new TreeNode<>(r.getMenuId(), r.getParentId(), r.getPerms(), r.getSort());
                    node.setExtra(Map.of(
                            "meta", meta,
                            "component", r.getComponent(),
                            "path", r.getPath()
                    ));
                    return node;
                })
                .collect(Collectors.toList());

        // ??? ???????????????????????? ??????????????????
        var treeMenus = TreeUtil.build(nodeList, 0L);

        // ?????????????????????
        var buttons = userMenus.stream()
                .distinct()
                .filter(r -> r.getMenuType() == BUTTON.getCode())
                .map(r -> new ButtonVO().setPerms(r.getPerms()).setTitle(r.getMenuName()))
                .collect(Collectors.toSet());

        return new UserRightsVO().setMenus(treeMenus).setButtons(buttons);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updatePassword(UpdatePasswordVO pwd) {
        var sysUser = baseMapper.selectById(pwd.getUserId());
        if (!StringUtils.equals(pwd.getNewPwd(), pwd.getConfirmPwd())) {
            throw new RuntimeException("??????????????????????????????????????????????????????");
        }
        // ?????? ????????????????????????
        if (matchesPassword(pwd.getOldPwd(), sysUser.getPassword())) {
            // ?????? ????????? ??? ?????????????????????
            if (matchesPassword(pwd.getNewPwd(), sysUser.getPassword())) {
                throw new RuntimeException("????????????????????????????????????????????????");
            } else {
                // ????????? ??????
                var newPassword = encryptPassword(pwd.getNewPwd());
                // ??????
                var pmsUser = PmsUser.builder()
                        .userId(pwd.getUserId())
                        .password(newPassword)
                        .build();
                return super.updateById(pmsUser);
            }
        }
        throw new RuntimeException("????????????????????????????????????");
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean resetPassword(ResetPasswordVO pwd) {
        var sysUser = baseMapper.selectById(pwd.getUserId());
        if (sysUser != null) {
            // ????????? ??????
            String newPassword = encryptPassword(pwd.getNewPwd());
            // ??????
            var pmsUser = new PmsUser()
                    .setUserId(pwd.getUserId())
                    .setPassword(newPassword);
            return super.updateById(pmsUser);
        }
        throw new RuntimeException("???????????????????????????????????????");
    }


}
