package com.xaaef.molly.core.auth.service.impl;

import cn.hutool.core.util.IdUtil;
import com.xaaef.molly.common.util.ServletUtils;
import com.xaaef.molly.core.auth.enums.GrantType;
import com.xaaef.molly.core.auth.exception.JwtAuthException;
import com.xaaef.molly.core.auth.jwt.JwtLoginUser;
import com.xaaef.molly.core.auth.jwt.JwtTokenValue;
import com.xaaef.molly.core.auth.po.LoginFormPO;
import com.xaaef.molly.core.auth.service.JwtTokenService;
import com.xaaef.molly.core.auth.service.LineCaptchaService;
import com.xaaef.molly.core.auth.service.UserLoginService;
import com.xaaef.molly.core.log.service.LogStorageService;
import com.xaaef.molly.core.tenant.util.TenantUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Slf4j
@Service
@AllArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {

    private final AuthenticationManager authManager;

    private final JwtTokenService tokenService;

    private final LogStorageService logStorageService;

    public final LineCaptchaService captchaService;

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
        target.setGrantType(GrantType.PASSWORD.getCode());
        target.setLoginTime(LocalDateTime.now());
        target.setTenantId(TenantUtils.getTenantId());

        // 生成一个随机ID 跟当前用户关联
        String loginId = IdUtil.simpleUUID();
        String token = tokenService.createJwtStr(loginId);
        target.setLoginId(loginId);

        var props = tokenService.getProps();

        tokenService.setLoginUser(target);

        // 保存登录日志到数据库中
        logStorageService.asyncLoginSave(target, ServletUtils.getRequestInfo(request));

        // 删除 redis 中的 验证码
        captchaService.delete(po.getCodeKey());

        return JwtTokenValue.builder()
                .header(props.getTokenHeader())
                .accessToken(token)
                .tokenType(props.getTokenType())
                .expiresIn(props.getTokenExpired())
                .build();
    }


}
