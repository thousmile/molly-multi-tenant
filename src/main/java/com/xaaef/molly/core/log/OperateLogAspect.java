package com.xaaef.molly.core.log;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.xaaef.molly.common.domain.CustomRequestInfo;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.common.util.ServletUtils;
import com.xaaef.molly.core.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.core.log.domain.OperLog;
import com.xaaef.molly.core.log.service.LogStorageService;
import com.xaaef.molly.core.log.service.impl.LogStorageServiceAdapter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * <p>
 * 操作日志记录处理
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/8/10 15:01
 */


@Slf4j
@Aspect
@Component
public class OperateLogAspect {

    private final LogStorageService logStorage;

    public OperateLogAspect(LogStorageService logStorage) {
        this.logStorage = logStorage;
    }

    @Value("${spring.application.name}")
    private String appName;

    /**
     * 统一切面日志,参数校验、统一异常处理、日志打印
     */
    @Pointcut("@annotation(com.xaaef.molly.core.log.OperateLog)")
    public void logPointCut() {
    }


    /**
     * 环绕增强，相当于MethodInterceptor
     */
    @Around("logPointCut()")
    public Object doAfterReturning(ProceedingJoinPoint joinPoint) {
        long startTime = System.currentTimeMillis();
        Object resp = null;
        // 耗时，单位毫秒
        long timeCost = 0L;
        try {
            resp = joinPoint.proceed();
            timeCost = System.currentTimeMillis() - startTime;
        } catch (Throwable ew) {
            log.error(ew.getMessage());
            resp = JsonResult.fail(ew.getMessage());
            //方法执行完成后增加日志
            addOperationLog(joinPoint, resp, ew, timeCost);
        } finally {
            //方法执行完成后增加日志
            addOperationLog(joinPoint, resp, null, timeCost);
        }
        return resp;
    }


    private void addOperationLog(JoinPoint joinPoint, Object resp, Throwable e, long timeCost) {
        var request = ServletUtils.getRequestInfo();
        var signature = (MethodSignature) joinPoint.getSignature();
        var annotation = signature.getMethod().getAnnotation(OperateLog.class);
        var operLog = new OperLog();
        // 全类名，方法名称
        var methodName = String.format("%s.%s()", signature.getDeclaringTypeName(), signature.getName());
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
        var userInfo = JwtSecurityUtils.getLoginUser();
        if (JwtSecurityUtils.isAuthenticated()) {
            operLog.setUserId(userInfo.getUserId());
            operLog.setNickname(userInfo.getNickname());
            operLog.setTenantId(userInfo.getTenantId());
            operLog.setCreateTime(LocalDateTime.now());
            operLog.setId(IdUtil.getSnowflakeNextId());
        }
        logStorage.asyncOperateSave(operLog);
    }


}
