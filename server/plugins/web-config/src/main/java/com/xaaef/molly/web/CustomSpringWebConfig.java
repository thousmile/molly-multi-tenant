package com.xaaef.molly.web;

import cn.hutool.core.net.Ipv4Util;
import com.xaaef.molly.common.consts.JwtConst;
import com.xaaef.molly.common.util.JsonUtils;
import com.xaaef.molly.tenant.ProjectIdInterceptor;
import com.xaaef.molly.tenant.TenantIdInterceptor;
import com.xaaef.molly.tenant.props.MultiTenantProperties;
import com.xaaef.molly.tenant.service.MultiTenantManager;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.format.Formatter;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.xaaef.molly.common.util.IpUtils.getLocalIPS;


@Slf4j
@Configuration
@AllArgsConstructor
public class CustomSpringWebConfig implements WebMvcConfigurer {

    private final ServerProperties sp;


    /**
     * TODO 输出文档地址
     *
     * @author WangChenChen
     * @date 2023/3/26 1:30
     */
    @PostConstruct
    public void outputDocAddress() {
        Set<String> localIPS = getLocalIPS();
        localIPS.add("localhost");
        localIPS.add(Ipv4Util.LOCAL_IP);
        localIPS.forEach(ip -> log.info("http://{}:{}/doc.html", ip, sp.getPort()));
    }


    private final TenantIdInterceptor tenantIdInterceptor;

    private final ProjectIdInterceptor projectIdInterceptor;

    private final MultiTenantProperties multiTenantProperties;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录接口，也需要，添加租户ID
        var whiteList = Arrays.stream(JwtConst.WHITE_LIST)
                .filter(s -> !JwtConst.LOGIN_URL.equals(s))
                .collect(Collectors.toList());

        // 启用 租户ID 拦截器
        if (multiTenantProperties.getEnable()) {
            registry.addInterceptor(tenantIdInterceptor)
                    .addPathPatterns("/**")
                    .excludePathPatterns(whiteList);
            log.info("Enable TenantIdInterceptor Success ...");
        }

        // 启用 项目ID 拦截器
        if (multiTenantProperties.getEnableProject()) {
            registry.addInterceptor(projectIdInterceptor)
                    .addPathPatterns("/**")
                    .excludePathPatterns(whiteList);
            log.info("Enable ProjectIdInterceptor Success ...");
        }

        WebMvcConfigurer.super.addInterceptors(registry);
    }


    @Bean
    @Primary
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JsonUtils.getMapper();
    }


    /**
     * 跨域过滤器
     *
     * @author WangChenChen
     * @date 2022/12/30 13:42
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.addExposedHeader("*");
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(configSource);
    }


    /***
     * Date日期类型转换器
     */
    @Bean
    public Formatter<Date> dateFormatter() {
        return new Formatter<>() {

            @Override
            public Date parse(String text, Locale locale) {
                Date date = null;
                try {
                    date = DateUtils.parseDate(text, locale, JsonUtils.DEFAULT_DATE_TIME_PATTERN);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                return date;
            }

            @Override
            public String print(Date date, Locale locale) {
                return DateFormatUtils.format(date, JsonUtils.DEFAULT_DATE_TIME_PATTERN, TimeZone.getDefault(), locale);
            }
        };
    }

    @Bean
    public Formatter<LocalDate> localDateFormatter() {
        return new Formatter<>() {
            @Override
            public LocalDate parse(String text, Locale locale) {
                return LocalDate.parse(text, DateTimeFormatter.ofPattern(JsonUtils.DEFAULT_DATE_PATTERN, locale));
            }

            @Override
            public String print(LocalDate object, Locale locale) {
                return DateTimeFormatter.ofPattern(JsonUtils.DEFAULT_DATE_PATTERN, locale).format(object);
            }
        };
    }

    @Bean
    public Formatter<LocalTime> localTimeFormatter() {
        return new Formatter<>() {
            @Override
            public LocalTime parse(String text, Locale locale) {
                return LocalTime.parse(text, DateTimeFormatter.ofPattern(JsonUtils.DEFAULT_TIME_PATTERN, locale));
            }

            @Override
            public String print(LocalTime object, Locale locale) {
                return DateTimeFormatter.ofPattern(JsonUtils.DEFAULT_TIME_PATTERN, locale).format(object);
            }
        };
    }

    @Bean
    public Formatter<LocalDateTime> localDateTimeFormatter() {
        return new Formatter<>() {

            @Override
            public String print(LocalDateTime localDateTime, Locale locale) {
                return DateTimeFormatter.ofPattern(JsonUtils.DEFAULT_DATE_TIME_PATTERN, locale).format(localDateTime);
            }

            @Override
            public LocalDateTime parse(String text, Locale locale) {
                return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(JsonUtils.DEFAULT_DATE_TIME_PATTERN, locale));
            }
        };
    }

}
