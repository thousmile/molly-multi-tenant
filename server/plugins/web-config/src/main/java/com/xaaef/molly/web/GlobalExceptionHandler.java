package com.xaaef.molly.web;

import com.xaaef.molly.auth.enums.OAuth2Error;
import com.xaaef.molly.auth.exception.JwtAuthException;
import com.xaaef.molly.common.exception.BizException;
import com.xaaef.molly.common.util.JsonResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
     * 数据转换异常
     *
     * @param e
     */
    @ExceptionHandler(value = HttpMessageConversionException.class)
    public JsonResult<String> exceptionHandler(HttpMessageConversionException e) {
        return JsonResult.fail(e.getMessage());
    }


    /**
     * 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResult<String> exceptionHandler(MethodArgumentNotValidException e) {
        var fieldError = e.getBindingResult().getFieldError();
        var errorMessage = fieldError != null ? fieldError.getDefaultMessage() : e.getMessage();
        return JsonResult.error(e.getStatusCode().value(), errorMessage);
    }


    /**
     * 参数缺失异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public JsonResult<String> exceptionHandler(MethodArgumentTypeMismatchException e) {
        return JsonResult.fail(e.getMessage());
    }


    /**
     * jwt认证异常
     *
     * @param e
     */
    @ExceptionHandler(value = JwtAuthException.class)
    public JsonResult<String> exceptionHandler(JwtAuthException e) {
        return JsonResult.error(e.getStatus(), e.getMessage());
    }


    /**
     * 空指针异常
     *
     * @param e
     */
    @ExceptionHandler(value = NullPointerException.class)
    public JsonResult<String> exceptionHandler(NullPointerException e) {
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
     * 业务 异常
     *
     * @param e
     */
    @ExceptionHandler(value = BizException.class)
    public JsonResult<String> exceptionHandler(BizException e) {
        return JsonResult.fail(e.getMessage());
    }


    /**
     * 运行时异常
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
        if (e instanceof ErrorResponse er) {
            return JsonResult.error(er.getStatusCode().value(), e.getMessage());
        }
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        return JsonResult.error(status, e.getMessage());
    }


}
