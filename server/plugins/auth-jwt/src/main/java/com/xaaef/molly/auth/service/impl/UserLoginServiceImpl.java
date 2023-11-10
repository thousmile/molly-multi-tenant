package com.xaaef.molly.auth.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.xaaef.molly.auth.enums.GrantType;
import com.xaaef.molly.auth.exception.JwtAuthException;
import com.xaaef.molly.auth.jwt.JwtLoginUser;
import com.xaaef.molly.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.auth.jwt.JwtTokenValue;
import com.xaaef.molly.auth.jwt.StringGrantedAuthority;
import com.xaaef.molly.auth.po.LoginFormPO;
import com.xaaef.molly.auth.service.JwtTokenService;
import com.xaaef.molly.auth.service.LineCaptchaService;
import com.xaaef.molly.auth.service.UserLoginService;
import com.xaaef.molly.common.domain.CustomRequestInfo;
import com.xaaef.molly.common.enums.AdminFlag;
import com.xaaef.molly.common.enums.StatusEnum;
import com.xaaef.molly.common.enums.UserType;
import com.xaaef.molly.common.util.ServletUtils;
import com.xaaef.molly.internal.api.*;
import com.xaaef.molly.internal.dto.LoginLogDTO;
import com.xaaef.molly.internal.dto.PmsRoleDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xaaef.molly.common.util.JsonUtils.DEFAULT_DATE_TIME_PATTERN;


/**
 * <p>
 * 用户登录
 * </p>
 *
 * @author Wang Chen Chen<932560435@qq.com>
 * @version 1.0.1
 * @createTime 2020/3/5 0005 11:32
 */

@Slf4j
@Service
@AllArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {

    private final AuthenticationManager authManager;

    private final JwtTokenService tokenService;

    private final ApiLogStorageService logStorageService;

    public final LineCaptchaService captchaService;

    private final ApiPmsRoleService roleService;

    private final ApiSysMenuService menuService;

    private final ApiSysTenantService tenantService;

    private final ApiSysUserService sysUserService;

    private final ApiCmsProjectService cmsProjectService;


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
        // 判断租户是否存在
        var currentTenant = tenantService.getByCurrentTenant();
        if (currentTenant == null) {
            throw new JwtAuthException("租户不存在！");
        }
        // 判断租户状态
        if (!Objects.equals(currentTenant.getStatus(), StatusEnum.NORMAL.getCode())) {
            throw new JwtAuthException(StrUtil.format("租户 {} 被禁用！", currentTenant.getName()));
        }
        // 判断租户是否过期
        if (LocalDateTime.now().isAfter(currentTenant.getExpired())) {
            var format = LocalDateTimeUtil.format(currentTenant.getExpired(), DEFAULT_DATE_TIME_PATTERN);
            throw new JwtAuthException(StrUtil.format("租户 {} 已经在 {} 过期了！", currentTenant.getName(), format));
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
        target.setTenantId(currentTenant.getTenantId());
        // 生成一个随机ID 跟当前用户关联
        target.setLoginId(IdUtil.getSnowflakeNextIdStr());

        // 判断当前登录的用户类型。系统用户 还是 租户用户
        String defaultTenantId = tenantService.getByDefaultTenantId();
        var userType = StringUtils.equals(defaultTenantId, target.getTenantId())
                ? UserType.SYSTEM : UserType.TENANT;
        target.setUserType(userType);

        target.setHaveProjectIds(new HashSet<>());
        target.setHaveTenantIds(new HashSet<>());

        // 如果当前登录的用户，是否系统用户
        if (userType == UserType.SYSTEM) {
            var haveTenantIds = sysUserService.listHaveTenantIds(target.getUserId());
            target.setHaveTenantIds(haveTenantIds);
        } else {
            // 非管理员 用户。根据部门Id获取关联的项目Id
            if (target.getAdminFlag() == AdminFlag.NO) {
                // 获取当前登录的用户 项目ID 列表
                var haveProjectIds = cmsProjectService.listProjectByDeptId(target.getDeptId());
                target.setHaveProjectIds(haveProjectIds);
            }
        }

        // 设置角色和菜单权限
        setAuthoritys(target);

        tokenService.setLoginUser(target);

        // 保存登录日志到数据库中
        asyncLoginLogSave(target, ServletUtils.getRequestInfo(request));

        // 删除 redis 中的 验证码
        captchaService.delete(po.getCodeKey());

        var props = tokenService.getProps();

        return JwtTokenValue.builder()
                .header(props.getTokenHeader())
                .accessToken(tokenService.createJwtStr(target.getLoginId()))
                .tokenType(props.getTokenType())
                .expiresIn(props.getTokenExpired())
                .build();
    }


    @Override
    public void refreshAuthoritys() {
        // 判断用户是否登录
        if (JwtSecurityUtils.isAuthenticated()) {
            refreshAuthoritys(JwtSecurityUtils.getLoginUser());
        }
    }


    @Override
    public void refreshAuthoritys(JwtLoginUser target) {
        target.setAuthorities(List.of());
        // 设置用户的权限
        setAuthoritys(target);
        // 更新用户的权限
        tokenService.updateLoginUser(target);
    }


    /**
     * 设置用户的权限
     *
     * @author WangChenChen
     * @date 2023/2/6 15:56
     */
    private void setAuthoritys(JwtLoginUser target) {
        // 获取 登录用户，拥有的角色
        var roles = roleService.listByUserId(target.getUserId());
        if (roles.isEmpty()) {
            target.setRoles(new HashSet<>());
            return;
        }
        target.setRoles(roles);
        // 判断当前 登录的用户，是不是管理员
        // 系统管理员。那么就获取所有除了租户的菜单。
        // 租户管理员。根据租户关联的模板，获取所有的菜单
        if (target.getAdminFlag() == AdminFlag.YES) {
            // 判断当前登录的用户，是不是系统用户
            Set<StringGrantedAuthority> authorities;
            if (target.getUserType() == UserType.SYSTEM) {
                authorities = menuService.listPermsByNonTenant()
                        .stream()
                        .map(StringGrantedAuthority::new)
                        .collect(Collectors.toSet());
            } else {
                authorities = menuService.listPermsByTenantId(target.getTenantId())
                        .stream()
                        .map(StringGrantedAuthority::new)
                        .collect(Collectors.toSet());
            }
            target.setAuthorities(authorities);
            return;
        }
        // 非管理员。就获取，角色关联的权限
        var roleIds = roles.stream().map(PmsRoleDTO::getRoleId).collect(Collectors.toSet());
        // 再根据，角色 获取关联的菜单ID
        var menuIds = roleService.listMenuIdByRoleIds(roleIds);
        if (!menuIds.isEmpty()) {
            var authorities = menuService.listPermsByMenuIds(menuIds)
                    .stream()
                    .map(StringGrantedAuthority::new)
                    .collect(Collectors.toSet());
            target.setAuthorities(authorities);
        }
    }


    /**
     * 保存 登录日志
     *
     * @author WangChenChen
     * @date 2023/2/6 15:56
     */
    private void asyncLoginLogSave(JwtLoginUser loginUser, CustomRequestInfo request) {
        var loginLog = new LoginLogDTO();
        BeanUtils.copyProperties(loginUser, loginLog);
        loginLog.setId(loginUser.getLoginId());
        loginLog.setRequestUrl(request.getRequestUrl());
        loginLog.setRequestIp(request.getIp());
        loginLog.setAddress(request.getAddress());
        loginLog.setOsName(request.getOsName());
        loginLog.setBrowser(request.getBrowser());
        loginLog.setGrantType(loginUser.getGrantType().getCode());
        loginLog.setCreateTime(loginUser.getLoginTime());
        logStorageService.asyncLoginSave(loginLog);
    }


}
