package com.xaaef.molly.core.xss;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * <p>
 *
 * </p>
 *
 * @author WangChenChen
 * @version 1.0
 * @date 2022/3/25 9:28
 */


@Configuration
public class CustomXssFilterConfig {

    @Bean
    public FilterRegistrationBean<Filter> xssFilterRegister() {
        var registration = new FilterRegistrationBean<>();
        registration.setFilter((servletRequest, servletResponse, filterChain) -> {
            var xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) servletRequest);
            filterChain.doFilter(xssRequest, servletResponse);
        });
        registration.addUrlPatterns("/*");
        registration.setName("XssFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public XssStringJsonDeserializer xssStringJsonDeserializer() {
        return new XssStringJsonDeserializer();
    }

}

