package com.xaaef.molly.web.repeat;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import com.xaaef.molly.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.common.util.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.xaaef.molly.common.consts.LoginConst.NO_REPEAT_SUBMIT;

/**
 * TODO 重复请求的拦截器
 *
 * @author WangChenChen
 * @date 2023/10/31 19:30
 */


@Slf4j
@Component
@AllArgsConstructor
public class NoRepeatSubmitInterceptor implements HandlerInterceptor {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        if (JwtSecurityUtils.isAuthenticated() && handler instanceof HandlerMethod method) {
            // 标注在方法上的 @NoRepeatSubmit
            var repeatSubmitByMethod = AnnotationUtils.findAnnotation(method.getMethod(), NoRepeatSubmit.class);
            // 标注在 controller 类上的 @NoRepeatSubmit
            var repeatSubmitByCls = AnnotationUtils.findAnnotation(method.getMethod().getDeclaringClass(), NoRepeatSubmit.class);
            // 没有限制重复提交，直接跳过
            if (Objects.isNull(repeatSubmitByMethod) && Objects.isNull(repeatSubmitByCls)) {
                return HandlerInterceptor.super.preHandle(request, response, handler);
            }
            var redisKey = String.format("%s:%s:%s:%s",
                    NO_REPEAT_SUBMIT,
                    JwtSecurityUtils.getLoginId(),
                    request.getMethod(),
                    HexUtil.encodeHexStr(request.getRequestURI())
            );
            var ms = Objects.nonNull(repeatSubmitByMethod) ? repeatSubmitByMethod.value() : repeatSubmitByCls.value();
            //存在即返回false，不存在即返回true
            var ifAbsent = stringRedisTemplate.opsForValue().setIfAbsent(redisKey, StrUtil.EMPTY, ms, TimeUnit.MILLISECONDS);
            //如果存在，表示已经请求过了，直接抛出异常，由全局异常进行处理返回指定信息
            if (ifAbsent != null && !ifAbsent) {
                ServletUtils.renderError(response, JsonResult.fail("不允许重复提交，请稍候再试！"));
                return false;
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
