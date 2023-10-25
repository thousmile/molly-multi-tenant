package com.xaaef.molly.auth.jwt;

import com.xaaef.molly.auth.enums.OAuth2Error;
import com.xaaef.molly.auth.service.JwtTokenService;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.common.util.ServletUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;

import static com.xaaef.molly.auth.enums.OAuth2Error.ACCESS_TOKEN_INVALID;
import static com.xaaef.molly.common.consts.JwtConst.WHITE_LIST;


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
public class JwtAuthTokenFilter extends BasicAuthenticationFilter {

    private final JwtTokenProperties props;

    private final JwtTokenService tokenService;

    public JwtAuthTokenFilter(JwtTokenProperties props,
                              JwtTokenService tokenService,
                              AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.props = props;
        this.tokenService = tokenService;
    }

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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 判断此路径 是否不校验忽略
        if (urlIgnore(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        // 获取 Request 中的请求头为 “ Authorization ” 的 token 值
        String bearerToken = request.getHeader(props.getTokenHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(props.getTokenType())) {
            try {
                // 根据 token 去 redis 中查询 user 数据，足够信任token的情况下，可以省略这一步
                var userDetails = tokenService.validate(bearerToken);
                var authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 将用户信息，设置到 SecurityContext 中，可以在任何地方 使用 下面语句获取 获取 当前用户登录信息
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
                ServletUtils.renderError(response, OAuth2Error.OAUTH2_EXCEPTION.getStatus(), ex.getMessage());
            }
        } else {
            var error = JsonResult.error(ACCESS_TOKEN_INVALID.getStatus(), ACCESS_TOKEN_INVALID.getError());
            ServletUtils.renderError(response, error);
        }
    }


    // spring 路径匹配工具
    private static final PathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * @param url 需要验证的 url 地址
     * @author Wang Chen Chen
     * @date 2021/7/9 10:14
     */
    public boolean urlIgnore(String url) {
        for (String pattern : WHITE_LIST) {
            if (PATH_MATCHER.match(pattern, url)) {
                return true;
            }
        }
        for (String pattern : props.getExcludePath()) {
            if (PATH_MATCHER.match(pattern, url)) {
                return true;
            }
        }
        return false;
    }


}
