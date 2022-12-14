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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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


    @Async
    @Override
    public void asyncLoginSave(JwtLoginUser loginUser, CustomRequestInfo request) {
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



    @Override
    public void asyncOperateSave(OperLog operLog) {
        log.info("操作日志: \n{}", JsonUtils.toFormatJson(operLog));
    }


}
