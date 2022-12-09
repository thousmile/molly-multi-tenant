package com.xaaef.molly.core.log;

import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.common.util.ServletUtils;
import com.xaaef.molly.core.log.service.LogStorageService;
import com.xaaef.molly.core.log.service.impl.LogStorageServiceAdapter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;


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
@AllArgsConstructor
@Import({
        LogStorageServiceAdapter.class
})
public class OperateLogAspect {

    private final LogStorageService logStorage;

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
        // 保存 操作日志
        logStorage.asyncOperateSave(joinPoint, resp, e, timeCost, ServletUtils.getRequestInfo());
    }


}
