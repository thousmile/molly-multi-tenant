package com.xaaef.molly.core.auth.jwt;

import com.xaaef.molly.common.util.ServletUtils;
import com.xaaef.molly.core.auth.enums.OAuth2Error;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler, AuthenticationEntryPoint {

    /**
     * 权限不足，
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) throws IOException {
        var msg = String.format("请求访问：%s，权限不足，请联系管理员", request.getRequestURI());
        ServletUtils.renderError(response, OAuth2Error.ACCESS_DENIED.getStatus(), msg);
    }


    /**
     * 未认证
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        var msg = String.format("请求访问：%s，认证失败，无法访问系统资源", request.getRequestURI());
        ServletUtils.renderError(response, OAuth2Error.OAUTH2_EXCEPTION.getStatus(), msg);
    }

}
