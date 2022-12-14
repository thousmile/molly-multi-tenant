package com.xaaef.molly.core.web;

import com.xaaef.molly.common.util.JsonUtils;
import com.xaaef.molly.core.tenant.TenantIdInterceptor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
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

import static com.xaaef.molly.core.auth.consts.JwtConst.LOGIN_URL;
import static com.xaaef.molly.core.auth.consts.JwtConst.WHITE_LIST;


@Slf4j
@Configuration
@AllArgsConstructor
public class CustomSpringWebConfig implements WebMvcConfigurer {

    private final TenantIdInterceptor tenantIdInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录接口，也需要，添加租户ID
        var whiteList = Arrays.stream(WHITE_LIST)
                .filter(s -> !LOGIN_URL.equals(s))
                .collect(Collectors.toList());
        registry.addInterceptor(tenantIdInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(whiteList);
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
