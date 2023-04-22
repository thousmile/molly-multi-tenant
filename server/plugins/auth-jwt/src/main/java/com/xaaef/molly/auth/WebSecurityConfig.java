package com.xaaef.molly.auth;

import com.xaaef.molly.common.util.ServletUtils;
import com.xaaef.molly.auth.enums.OAuth2Error;
import com.xaaef.molly.auth.jwt.JwtAuthTokenFilter;
import com.xaaef.molly.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.auth.jwt.JwtTokenProperties;
import com.xaaef.molly.auth.service.JwtTokenService;
import com.xaaef.molly.auth.service.impl.JwtTokenServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static com.xaaef.molly.common.consts.JwtConst.WHITE_LIST;


/**
 * <p>
 * spring Security 核心配置类
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 2.0
 * @date 2019/4/18 11:45
 */


@Slf4j
@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final JwtTokenProperties tokenProperties;

    private final UserDetailsService userDetailsService;


    /**
     * 认证管理器，登录的时候参数会传给 authenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }


    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(this.userDetailsService)
                .passwordEncoder(JwtSecurityUtils.getPasswordEncoder());
    }


    /**
     * TODO JWT token 保存服务端
     *
     * @author WangChenChen
     * @date 2022/12/10 16:30
     */
    @Bean
    public JwtTokenService jwtTokenService(RedisTemplate<String, Object> redisTemplate) {
        return new JwtTokenServiceImpl(tokenProperties, redisTemplate);
    }


    /**
     * TODO JWT token 认证 拦截器
     *
     * @author WangChenChen
     * @date 2022/12/10 16:30
     */
    @Bean
    public JwtAuthTokenFilter jwtAuthTokenFilter(JwtTokenService jwtTokenService,
                                                 AuthenticationManager authenticationManager) {
        return new JwtAuthTokenFilter(tokenProperties, jwtTokenService, authenticationManager);
    }


    /**
     * spring Security 配置
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtAuthTokenFilter jwtAuthTokenFilter) throws Exception {
        httpSecurity
                // 关闭 csrf
                .csrf().disable()
                // 允许跨域（也可以不允许，看具体需求）
                .cors().and()
                // 不需要 Session
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 自定义 认证的异常处理
                .exceptionHandling(ex -> ex
                        // 身份验证异常 处理
                        .authenticationEntryPoint((request, response, ex1) -> {
                            log.warn(ex1.getMessage());
                            var msg = String.format("请求访问：%s，认证失败，无法访问系统资源", request.getRequestURI());
                            ServletUtils.renderError(response, OAuth2Error.OAUTH2_EXCEPTION.getStatus(), msg);
                        })
                        // 权限不足异常 处理
                        .accessDeniedHandler((request, response, ex2) -> {
                            log.warn(ex2.getMessage());
                            var msg = String.format("请求访问：%s，权限不足，请联系管理员", request.getRequestURI());
                            ServletUtils.renderError(response, OAuth2Error.ACCESS_DENIED.getStatus(), msg);
                        })
                )
                .authorizeHttpRequests(a -> a
                        // 跨域的调用
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        //  获取白名单（不进行权限验证）
                        .requestMatchers(tokenProperties.getExcludePath()).permitAll()
                        //  无需认证的固定的白名单
                        .requestMatchers(WHITE_LIST).permitAll()
                        //  其他的请求全部要认证
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(jwtAuthTokenFilter, BasicAuthenticationFilter.class)
                .headers()
                .frameOptions()
                .disable()
                // 开始 xss 保护
                .xssProtection()
                .and()
                .cacheControl();
        return httpSecurity.build();
    }


}
