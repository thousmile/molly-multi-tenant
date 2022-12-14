package com.xaaef.molly.common.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.xaaef.molly.common.domain.CustomRequestInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


/**
 * <p>
 *
 * </p>
 *
 * @author WangChenChen
 * @version 1.2
 * @date 2022/5/26 10:24
 */


@Slf4j
public class ServletUtils {

    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }


    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }


    /**
     * 获取response
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }


    /**
     * 获取String参数
     */
    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }


    /**
     * 获取客户端信息
     *
     * @return
     */
    public static UserAgent getUserAgent() {
        String ua = getRequest().getHeader(HttpHeaders.USER_AGENT);
        return UserAgentUtil.parse(ua);
    }


    /**
     * 获取客户端信息
     *
     * @return
     */
    public static UserAgent getUserAgent(HttpServletRequest request) {
        String ua = request.getHeader(HttpHeaders.USER_AGENT);
        return UserAgentUtil.parse(ua);
    }


    /**
     * 获取客户端信息
     */
    public static UserAgent getUserAgent(String ua) {
        return UserAgentUtil.parse(ua);
    }


    /**
     * 获取 请求全路径
     */
    public static String getFullPath(HttpServletRequest request) {
        // 经过网关 需要带上前缀
        String prefix = request.getHeader("x-forwarded-prefix");
        StringBuilder url = new StringBuilder(prefix == null ? "" : prefix);
        url.append(request.getRequestURI());
        if (!StringUtils.isEmpty(request.getQueryString())) {
            url.append("?").append(request.getQueryString());
        }
        return url.toString();
    }


    /**
     * 获取 Http Request 客户端的参数
     */
    public static CustomRequestInfo getRequestInfo() {
        var request = getRequest();
        return getRequestInfo(request);
    }


    /**
     * 获取 Http Request 客户端的参数
     */
    public static CustomRequestInfo getRequestInfo(HttpServletRequest request) {
        var fullPath = getFullPath(request);
        var userAgent = getUserAgent(request);
        var browser = StrUtil.format("{} {}", userAgent.getBrowser(), userAgent.getVersion());
        var os = StrUtil.format("{} {}", userAgent.getPlatform(), Objects.requireNonNullElse(userAgent.getOsVersion(), ""));
        var ipAddr = IpUtils.getIpAddr(request);
        var realAddress = IpUtils.getRealAddressByIP(ipAddr);
        return CustomRequestInfo.builder()
                .method(request.getMethod())
                .requestUrl(fullPath)
                .osName(os)
                .browser(browser)
                .ip(ipAddr)
                .address(realAddress)
                .build();
    }


    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string   待渲染的字符串
     * @return null
     */
    public static void renderString(HttpServletResponse response, String string) {
        try {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().println(string);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("renderString: {}", e.getMessage());
        }
    }


    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param ex       待渲染的字符串
     * @return null
     */
    public static void renderError(HttpServletResponse response, JsonResult result) {
        renderString(response, JsonUtils.toJson(result));
    }


    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param ex       待渲染的字符串
     * @return null
     */
    public static void renderError(HttpServletResponse response, int status, String message) {
        String json = JsonUtils.toJson(JsonResult.error(status, message));
        renderString(response, json);
    }


}
