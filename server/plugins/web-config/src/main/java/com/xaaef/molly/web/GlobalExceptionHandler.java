package com.xaaef.molly.web;

import com.xaaef.molly.auth.enums.OAuth2Error;
import com.xaaef.molly.auth.exception.JwtAuthException;
import com.xaaef.molly.common.util.JsonResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>
 * 全局异常处理
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/13 13:47
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常
     *
     * @param e
     */
    @ExceptionHandler(value = JwtAuthException.class)
    public JsonResult<String> bizExceptionHandler(JwtAuthException e) {
        log.error("发生业务异常！原因是： {}  {} ", e.getStatus(), e.getMessage());
        return JsonResult.error(e.getStatus(), e.getMessage());
    }


    /**
     * 参数校验统一异常处理
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResult<String> handleBindException(MethodArgumentNotValidException e) {
        var fieldError = e.getBindingResult().getFieldError();
        if (fieldError != null) {
            return JsonResult.fail(fieldError.getDefaultMessage());
        }
        return JsonResult.fail("");
    }


    /**
     * 处理空指针的异常
     *
     * @param e
     */
    @ExceptionHandler(value = NullPointerException.class)
    public JsonResult<String> exceptionHandler(NullPointerException e) {
        e.printStackTrace();
        log.error("发生空指针异常！原因是: {} ", e.getMessage());
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        return JsonResult.error(status, e.getMessage());
    }


    /**
     * 认证失败异常
     */
    @ExceptionHandler(value = AuthenticationException.class)
    public JsonResult<String> exceptionHandler(HttpServletRequest request, AuthenticationException e) {
        var msg = String.format("请求访问：%s，认证失败，无法访问系统资源", request.getRequestURI());
        return JsonResult.error(OAuth2Error.OAUTH2_EXCEPTION.getStatus(), msg);
    }


    /**
     * 权限不足异常
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public JsonResult<String> exceptionHandler(HttpServletRequest request, AccessDeniedException e) {
        var msg = String.format("请求访问：%s，权限不足，请联系管理员", request.getRequestURI());
        return JsonResult.error(OAuth2Error.ACCESS_DENIED.getStatus(), msg);
    }


    /**
     * 数据转换异常
     *
     * @param e
     */
    @ExceptionHandler(value = HttpMessageConversionException.class)
    public JsonResult<String> exceptionHandler(HttpMessageConversionException e) {
        return JsonResult.fail(e.getMessage());
    }


    /**
     * 处理运行时的异常
     *
     * @param e
     */
    @ExceptionHandler(value = RuntimeException.class)
    public JsonResult<String> exceptionHandler(RuntimeException e) {
        return JsonResult.fail(e.getMessage());
    }


    /**
     * 处理其他异常
     *
     * @param e
     */
    @ExceptionHandler(value = Exception.class)
    public JsonResult<String> exceptionHandler(Exception e) {
        e.printStackTrace();
        log.error("未知异常！原因是: {} ", e.getMessage());
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        return JsonResult.error(status, e.getMessage());
    }

}
