package com.xaaef.molly.web.log;

import com.xaaef.molly.common.util.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

/**
 * <p>
 * Hibernate-Validator 错误结果处理切面
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 2.0
 * @date 2019/4/18 11:45
 */

@Slf4j
@Aspect
@Component
public class BindingResultAspect {

    @Pointcut("execution(public * com.xaaef.molly.*.controller..*.*(..))")
    public void BindingResult1() {
    }

    @Pointcut("execution(public * com.xaaef.molly.controller..*.*(..))")
    public void BindingResult2() {
    }

    @Around("BindingResult1() || BindingResult2()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        var args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult result) {
                if (result.hasErrors()) {
                    var fieldError = result.getFieldError();
                    if (fieldError != null) {
                        return JsonResult.fail(fieldError.getDefaultMessage());
                    } else {
                        return JsonResult.fail("请求参数错误！");
                    }
                }
            }
        }
        return joinPoint.proceed();
    }

}
