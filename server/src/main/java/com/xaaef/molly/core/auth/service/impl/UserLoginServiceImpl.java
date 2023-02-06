package com.xaaef.molly.core.auth.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaaef.molly.common.domain.CustomRequestInfo;
import com.xaaef.molly.common.util.ServletUtils;
import com.xaaef.molly.core.auth.enums.AdminFlag;
import com.xaaef.molly.core.auth.enums.GrantType;
import com.xaaef.molly.core.auth.enums.StatusEnum;
import com.xaaef.molly.core.auth.enums.UserType;
import com.xaaef.molly.core.auth.exception.JwtAuthException;
import com.xaaef.molly.core.auth.jwt.JwtLoginUser;
import com.xaaef.molly.core.auth.jwt.JwtTokenValue;
import com.xaaef.molly.core.auth.jwt.StringGrantedAuthority;
import com.xaaef.molly.core.auth.po.LoginFormPO;
import com.xaaef.molly.core.auth.service.JwtTokenService;
import com.xaaef.molly.core.auth.service.LineCaptchaService;
import com.xaaef.molly.core.auth.service.UserLoginService;
import com.xaaef.molly.core.log.domain.LoginLog;
import com.xaaef.molly.core.log.service.LogStorageService;
import com.xaaef.molly.core.tenant.service.MultiTenantManager;
import com.xaaef.molly.core.tenant.util.TenantUtils;

import com.xaaef.molly.perms.entity.PmsRoleProxy;
import com.xaaef.molly.perms.mapper.PmsRoleMapper;
import com.xaaef.molly.system.entity.SysMenu;
import com.xaaef.molly.system.enums.MenuTargetEnum;
import com.xaaef.molly.system.mapper.SysMenuMapper;
import com.xaaef.molly.system.mapper.SysTenantMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    private final LogStorageService logStorageService;

    public final LineCaptchaService captchaService;

    public final MultiTenantManager tenantManager;

    private final PmsRoleMapper roleMapper;

    private final SysMenuMapper menuMapper;

    private final SysTenantMapper tenantMapper;

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
        var currentTenant = tenantMapper.selectById(TenantUtils.getTenantId());
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
        asyncLoginLogSave(target, ServletUtils.getRequestInfo(request));

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


    /**
     * 保存 登录日志
     *
     * @author WangChenChen
     * @date 2023/2/6 15:56
     */
    private void asyncLoginLogSave(JwtLoginUser loginUser, CustomRequestInfo request) {
        var loginLog = new LoginLog();
        loginLog.setRequestUrl(request.getRequestUrl());
        loginLog.setRequestIp(request.getIp());
        loginLog.setAddress(request.getAddress());
        loginLog.setOsName(request.getOsName());
        loginLog.setBrowser(request.getBrowser());
        loginLog.setAvatar(loginUser.getAvatar());
        loginLog.setNickname(loginUser.getNickname());
        loginLog.setUsername(loginUser.getUsername());
        loginLog.setUserId(loginUser.getUserId());
        loginLog.setTenantId(loginUser.getTenantId());
        loginLog.setCreateTime(loginUser.getLoginTime());
        loginLog.setGrantType(loginUser.getGrantType().getCode());
        loginLog.setId(IdUtil.getSnowflakeNextIdStr());
        logStorageService.asyncLoginSave(loginLog);
    }


}
