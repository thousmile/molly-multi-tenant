package com.xaaef.molly.core.auth.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaaef.molly.common.util.ServletUtils;
import com.xaaef.molly.core.auth.enums.AdminFlag;
import com.xaaef.molly.core.auth.enums.GrantType;
import com.xaaef.molly.core.auth.enums.UserType;
import com.xaaef.molly.core.auth.exception.JwtAuthException;
import com.xaaef.molly.core.auth.jwt.JwtLoginUser;
import com.xaaef.molly.core.auth.jwt.JwtTokenValue;
import com.xaaef.molly.core.auth.jwt.StringGrantedAuthority;
import com.xaaef.molly.core.auth.po.LoginFormPO;
import com.xaaef.molly.core.auth.service.JwtTokenService;
import com.xaaef.molly.core.auth.service.LineCaptchaService;
import com.xaaef.molly.core.auth.service.UserLoginService;
import com.xaaef.molly.core.log.service.LogStorageService;
import com.xaaef.molly.core.tenant.service.MultiTenantManager;
import com.xaaef.molly.core.tenant.util.TenantUtils;

import javax.servlet.http.HttpServletRequest;

import com.xaaef.molly.perms.entity.PmsRoleProxy;
import com.xaaef.molly.perms.mapper.PmsRoleMapper;
import com.xaaef.molly.system.entity.SysMenu;
import com.xaaef.molly.system.enums.MenuTargetEnum;
import com.xaaef.molly.system.mapper.SysMenuMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {

    private final AuthenticationManager authManager;

    private final JwtTokenService tokenService;

    private final LogStorageService logStorageService;

    public final LineCaptchaService captchaService;

    public final MultiTenantManager tenantManager;

    private final PmsRoleMapper roleMapper;

    private final SysMenuMapper menuMapper;

    /**
     * 登录表单获取 Token
     *
     * @return
     * @date 2018/11/16
     * @author Wang Chen Chen
     */
    @Override
    public JwtTokenValue login(LoginFormPO po, HttpServletRequest request) throws JwtAuthException {
        // 验证码是否正确
        if (!captchaService.check(po.getCodeKey(), po.getCodeText())) {
            throw new JwtAuthException("认证失败：验证码错误。");
        }

        // 把表单提交的 username  password 封装到 UsernamePasswordAuthenticationToken中
        var authResult = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        po.getUsername(),
                        po.getPassword()
                )
        );

        var target = new JwtLoginUser();
        BeanUtils.copyProperties(authResult.getPrincipal(), target);
        target.setGrantType(GrantType.PASSWORD);
        target.setLoginTime(LocalDateTime.now());
        target.setTenantId(TenantUtils.getTenantId());

        // 判断当前登录的用户类型。系统用户 还是 租户用户
        var userType = StringUtils.equals(tenantManager.getDefaultTenantId(), target.getTenantId())
                ? UserType.SYSTEM : UserType.TENANT;
        target.setUserType(userType);

        // 生成一个随机ID 跟当前用户关联
        String loginId = IdUtil.simpleUUID();
        String token = tokenService.createJwtStr(loginId);
        target.setLoginId(loginId);

        // 设置角色和菜单权限
        setAuthoritys(target);

        tokenService.setLoginUser(target);

        // 保存登录日志到数据库中
        logStorageService.asyncLoginSave(target, ServletUtils.getRequestInfo(request));

        // 删除 redis 中的 验证码
        captchaService.delete(po.getCodeKey());

        var props = tokenService.getProps();

        return JwtTokenValue.builder()
                .header(props.getTokenHeader())
                .accessToken(token)
                .tokenType(props.getTokenType())
                .expiresIn(props.getTokenExpired())
                .build();
    }


    private void setAuthoritys(JwtLoginUser target) {
        // 获取 登录用户，拥有的角色
        var roles = roleMapper.selectListByUserIds(Set.of(target.getUserId()));
        if (roles.isEmpty()) {
            return;
        }
        roles.forEach(r -> r.setUserId(null));
        target.setRoles(roles);
        // 判断当前 登录的用户，是不是管理员
        // 系统管理员。那么就获取所有除了租户的菜单。
        // 租户管理员。根据租户关联的模板，获取所有的菜单
        if (target.getAdminFlag() == AdminFlag.YES) {
            // 判断当前登录的用户，是不是系统用户
            if (target.getUserType() == UserType.SYSTEM) {
                var wrapper = new LambdaQueryWrapper<SysMenu>()
                        .select(SysMenu::getPerms)
                        .ne(SysMenu::getTarget, MenuTargetEnum.TENANT.getCode());
                var authorities = menuMapper.selectList(wrapper).stream()
                        .map(SysMenu::getPerms)
                        .map(StringGrantedAuthority::new)
                        .collect(Collectors.toSet());
                target.setAuthorities(authorities);
            } else {
                var authorities = menuMapper.selectPermsByTenantId(target.getTenantId())
                        .stream()
                        .map(StringGrantedAuthority::new)
                        .collect(Collectors.toSet());
                target.setAuthorities(authorities);
            }
            return;
        }
        // 非管理员。就获取，角色关联的权限
        var roleIds = roles.stream().map(PmsRoleProxy::getRoleId).collect(Collectors.toSet());
        // 再根据，角色 获取关联的菜单ID
        var menuIds = roleMapper.selectMenuIdByRoleIds(roleIds);
        if (!menuIds.isEmpty()) {
            var wrapper = new LambdaQueryWrapper<SysMenu>()
                    .select(SysMenu::getPerms)
                    .in(SysMenu::getMenuId, menuIds);
            var authorities = menuMapper.selectList(wrapper)
                    .stream()
                    .map(SysMenu::getPerms)
                    .map(StringGrantedAuthority::new)
                    .collect(Collectors.toSet());
            target.setAuthorities(authorities);
        }

    }


}
