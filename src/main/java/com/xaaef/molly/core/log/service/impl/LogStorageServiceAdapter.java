package com.xaaef.molly.core.log.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.xaaef.molly.common.domain.CustomRequestInfo;
import com.xaaef.molly.common.util.JsonUtils;
import com.xaaef.molly.core.auth.jwt.JwtLoginUser;
import com.xaaef.molly.core.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.core.log.OperateLog;
import com.xaaef.molly.core.log.domain.LoginLog;
import com.xaaef.molly.core.log.domain.OperLog;
import com.xaaef.molly.core.log.service.LogStorageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 操作日志
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/8/17 16:18
 */


@Slf4j
@Component
public class LogStorageServiceAdapter implements LogStorageService {

    @Value("${spring.application.name}")
    private String appName;


    @Async
    @Override
    public void asyncLoginSave(JwtLoginUser loginUser, CustomRequestInfo request) {
        assert request != null;
        var loginLog = new LoginLog();
        loginLog.setRequestUrl(request.getRequestUrl());
        loginLog.setRequestIp(request.getIp());
        loginLog.setAddress(request.getAddress());
        loginLog.setOsName(request.getOsName());
        loginLog.setBrowser(request.getBrowser());
        loginLog.setAvatar(loginUser.getAvatar());
        loginLog.setNickname(loginUser.getNickname());
        loginLog.setUsername(loginUser.getUsername());
        loginLog.setUserId(loginUser.getUserId());
        loginLog.setTenantId(loginUser.getTenantId());
        loginLog.setCreateTime(loginUser.getLoginTime());
        loginLog.setGrantType(loginUser.getGrantType().getCode());
        loginLog.setId(IdUtil.getSnowflakeNextId());
        log.info("登录日志: \n{}", JsonUtils.toFormatJson(loginLog));
    }


    @Async
    @Override
    public void asyncOperateSave(JoinPoint joinPoint, Object resp, Throwable e, long timeCost, CustomRequestInfo request) {
        assert request != null;
        var signature = (MethodSignature) joinPoint.getSignature();
        var annotation = signature.getMethod().getAnnotation(OperateLog.class);
        var operLog = new OperLog();
        // 全类名，方法名称
        var methodName = String.format("%s.%s()", signature.getDeclaringTypeName(), signature.getName());
        var userInfo = JwtSecurityUtils.getLoginUser();
        operLog.setStatus(HttpStatus.OK.value());
        if (e != null) {
            operLog.setErrorLog(e.getMessage());
            operLog.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        operLog.setTitle(annotation.title());
        operLog.setOperType(annotation.type().name());
        operLog.setServiceName(appName);
        operLog.setMethod(methodName);
        if (joinPoint.getArgs() != null) {
            var params = new LinkedList<>();
            for (Object arg : joinPoint.getArgs()) {
                if (arg instanceof BindingResult
                        || arg instanceof HttpServletRequest
                        || arg instanceof HttpServletResponse
                        || arg instanceof HttpHeaders
                ) {
                    log.debug("spring mvc 对象...");
                } else {
                    params.add(arg);
                }
            }
            var methodArgs = params.stream()
                    .map(r -> BeanUtil.beanToMap(r, false, true))
                    .collect(Collectors.toList());
            operLog.setMethodArgs(methodArgs);
        }
        operLog.setRequestUrl(request.getRequestUrl());
        operLog.setRequestMethod(request.getMethod());
        operLog.setRequestIp(request.getIp());
        operLog.setAddress(request.getAddress());
        Map<String, Object> responseResultMap = BeanUtil.beanToMap(resp, false, true);
        operLog.setResponseResult(responseResultMap);
        operLog.setTimeCost(timeCost);
        operLog.setUserId(userInfo.getUserId());
        operLog.setNickname(userInfo.getNickname());
        operLog.setTenantId(userInfo.getTenantId());
        operLog.setCreateTime(LocalDateTime.now());
        operLog.setId(IdUtil.getSnowflakeNextId());
        log.info("操作日志: \n{}", JsonUtils.toFormatJson(operLog));
    }


}
