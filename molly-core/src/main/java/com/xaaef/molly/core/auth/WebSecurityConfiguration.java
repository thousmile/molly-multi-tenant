package com.xaaef.molly.core.auth;

import com.xaaef.molly.core.auth.jwt.JwtAccessDeniedHandler;
import com.xaaef.molly.core.auth.jwt.JwtAuthenticationTokenFilter;
import com.xaaef.molly.core.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.core.auth.jwt.JwtTokenProperties;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.List;

import static com.xaaef.molly.core.tenant.constant.MultiTenantName.X_PROJECT_ID;
import static com.xaaef.molly.core.tenant.constant.MultiTenantName.X_TENANT_ID;


/**
 * <p>
 * spring Security 核心配置类
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 2.0
 * @date 2019/4/18 11:45
 */


@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfiguration {

    private final AuthenticationConfiguration authConfiguration;

    private final JwtTokenProperties props;

    private final UserDetailsService userDetailsService;

    private final JwtAuthenticationTokenFilter tokenFilter;

    private final JwtAccessDeniedHandler accessDeniedHandler;


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
     * spring Security 配置
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // 关闭 csrf
        httpSecurity.csrf().disable()
                // 允许跨域（也可以不允许，看具体需求）
                .cors().and()
                .headers().frameOptions().disable().and()
                // 基于token，因此不须要session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .sessionFixation().none().and()
                //未授权处理
                .exceptionHandling()
                // 自定义未登录返回
                .authenticationEntryPoint(accessDeniedHandler)
                // 自定义无权限访问
                .accessDeniedHandler(accessDeniedHandler).and()
                .authorizeHttpRequests()
                // 跨域的调用
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                //  获取白名单（不进行权限验证）
                .requestMatchers(props.getExcludePath()).permitAll()
                //  固定的白名单
                .requestMatchers(
                        "/actuator/**",
                        "/v2/api-docs",
                        "/doc.html",
                        "/configuration/ui",
                        "/swagger-resources",
                        "/configuration/security",
                        "/webjars/**",
                        "/swagger-resources/configuration/ui",
                        "/swagger-ui.html",
                        "/error",
                        "/error/**"
                ).permitAll()
                //  其他的请求全部要认证
                .anyRequest()
                .authenticated()
                .and()
                .authenticationManager(authenticationManager(authConfiguration))
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .headers()
                .cacheControl();
        return httpSecurity.build();
    }


    /**
     * 跨域资源配置
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        // 此处发现如果不加入自己的项目地址，会被拦截。
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(
                List.of("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH")
        );
        configuration.setAllowedHeaders(
                List.of("Access-Control-Allow-Origin", "X-Requested-With",
                        "Origin", "Content-Type",
                        "Accept", "Authorization",
                        X_TENANT_ID, X_PROJECT_ID)
        );
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(Duration.ofHours(2));
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
