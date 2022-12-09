package com.xaaef.molly.perms.service.impl;

import cn.hutool.core.util.IdUtil;
import com.xaaef.molly.common.util.ServletUtils;
import com.xaaef.molly.core.auth.enums.GrantType;
import com.xaaef.molly.core.auth.exception.JwtAuthException;
import com.xaaef.molly.core.auth.jwt.JwtLoginUser;
import com.xaaef.molly.core.auth.jwt.JwtTokenValue;
import com.xaaef.molly.core.auth.service.JwtTokenService;
import com.xaaef.molly.core.log.service.LogStorageService;
import com.xaaef.molly.perms.po.LoginUserVO;
import com.xaaef.molly.perms.service.UserLoginService;
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


    /**
     * 登录表单获取 Token
     *
     * @return
     * @date 2018/11/16
     * @author Wang Chen Chen
     */
    @Override
    public JwtTokenValue login(LoginUserVO user, HttpServletRequest request) throws JwtAuthException {
        // 把表单提交的 username  password 封装到 UsernamePasswordAuthenticationToken中
        var source = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        var target = new JwtLoginUser();
        BeanUtils.copyProperties(source.getPrincipal(), target);
        target.setGrantType(GrantType.PASSWORD.getCode());
        target.setLoginTime(LocalDateTime.now());
        // 生成一个随机ID 跟当前用户关联
        String loginId = IdUtil.simpleUUID();
        String token = tokenService.createJwtStr(loginId);
        target.setLoginId(loginId);
        var props = tokenService.getProps();
        tokenService.setLoginUser(loginId, target);
        // 保存登录日志到数据库中
        logStorageService.asyncLoginSave(target, ServletUtils.getRequestInfo());
        return JwtTokenValue.builder()
                .header(props.getTokenHeader())
                .accessToken(token)
                .tokenType(props.getTokenType())
                .expiresIn(props.getTokenExpired())
                .build();
    }


}
