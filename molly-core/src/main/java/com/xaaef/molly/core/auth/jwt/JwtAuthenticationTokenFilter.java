package com.xaaef.molly.core.auth.jwt;

import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.common.util.ServletUtils;
import com.xaaef.molly.core.auth.enums.OAuth2Error;
import com.xaaef.molly.core.auth.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.xaaef.molly.core.auth.enums.OAuth2Error.ACCESS_TOKEN_INVALID;


/**
 * <p>
 * jwt核心拦截器
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 2.0
 * @date 2019/4/18 11:45
 */


@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private JwtTokenProperties props;

    private final JwtTokenService tokenService;

    /**
     * 获取当前登录用户的信息
     * <p>
     * JwtUserDetails userDetails = JwtSecurityUtils.getLoginUser()
     *
     * @throws IOException
     * @author Wang Chen Chen<932560435@qq.com>
     * @date 2019/12/19 0:11
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取 Request 中的请求头为 “ Authorization ” 的 token 值
        String bearerToken = request.getHeader(props.getTokenHeader());
        if (StringUtils.hasText(bearerToken)) {
            try {
                // 根据 token 去 redis 中查询 user 数据，足够信任token的情况下，可以省略这一步
                var userDetails = tokenService.validate(bearerToken);
                var authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 将用户信息，设置到 SecurityContext 中，可以在任何地方 使用 下面语句获取 获取 当前用户登录信息
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return;
            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
                ServletUtils.renderError(response, OAuth2Error.OAUTH2_EXCEPTION.getStatus(), ex.getMessage());
                return;
            }
        }
        var error = JsonResult.error(ACCESS_TOKEN_INVALID.getStatus(), ACCESS_TOKEN_INVALID.getError());
        ServletUtils.renderError(response, error);
    }


}
