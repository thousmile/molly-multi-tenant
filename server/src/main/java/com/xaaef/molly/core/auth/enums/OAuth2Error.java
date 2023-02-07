package com.xaaef.molly.core.auth.enums;

import lombok.Getter;

/**
 * <p>
 * 认证状态
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/5 9:31
 */

@Getter
public enum OAuth2Error {

    /**
     * 认证错误，不知道啥原因
     */
    OAUTH2_EXCEPTION(400004, "认证错误!"),

    /**
     * access_token 不存在
     */
    ACCESS_TOKEN_INVALID(400010, "access_token 不存在"),


    /**
     * access_token 过期
     */
    ACCESS_TOKEN_EXPIRED(400011, "access_token 已过期"),


    /**
     * token 格式错误
     */
    TOKEN_FORMAT_ERROR(400012, "access_token 格式错误"),


    /**
     * refresh_token 不存在
     */
    REFRESH_TOKEN_INVALID(400014, "refresh_token 不存在"),


    /**
     * refresh_token 过期
     */
    REFRESH_TOKEN_EXPIRED(400015, "refresh_token 已经过期"),


    /**
     * 此服务，必须是非客户端模式，授权才可以调用
     */
    TOKEN_USER_INVALID(400016, "此服务，必须是非客户端模式，授权才可以调用"),

    /**
     * 用户不存在
     */
    USER_INVALID(400020, "用户不存在"),

    /**
     * 用户密码错误
     */
    USER_PASSWORD_ERROR(400021, "用户密码错误"),

    /**
     * 当前用户被禁用
     */
    USER_DISABLE(400022, "当前用户被禁用"),

    /**
     * 当前用户被锁定
     */
    USER_LOCKING(400023, "当前用户被锁定"),

    /**
     * 用户手机号不存在
     */
    USER_MOBILE_INVALID(400024, "用户手机号不存在"),

    /**
     * 用户类型错误
     */
    USER_TYPES_ERROR(400025, "用户类型错误！"),

    /**
     * 客户端不存在
     */
    CLIENT_INVALID(400030, "客户端不存在"),

    /**
     * 客户端被禁用
     */
    CLIENT_DISABLE(400031, "客户端被禁用"),

    /**
     * 客户端秘钥错误
     */
    CLIENT_SECRET_ERROR(400032, "客户端秘钥错误"),

    /**
     * 授权类型错误
     */
    AUTHORIZATION_GRANT_TYPE(400033, "授权类型错误"),

    /**
     * 授权码模式中，使用code来换取 access_token，这个code不存在。
     */
    CODE_INVALID(400034, "code不存在"),

    /**
     * 授权码模式中，回调的域名和数据库的域名不一致
     */
    DOMAIN_NAME_ILLEGAL(400035, "回调的域名与设置的域名不一致"),

    /**
     * 客户端不相同
     */
    CLIENT_IS_DIFFERENT(400036, "客户端不一致"),

    /**
     * 请求参数解析错误
     */
    REQUEST_PARAM_VALIDATE(400044, "请求参数解析错误"),

    /**
     * 验证码错误
     */
    VERIFICATION_CODE_ERROR(400045, "验证码错误"),

    /**
     * 权限不足
     */
    ACCESS_DENIED(400061, "请求访问，权限不足"),


    /**
     * 租户ID不存在
     */
    TENANT_ID_DOES_NOT_EXIST(400444, "租户ID不存在"),


    /**
     * 请求必须添加租户ID
     */
    REQUEST_MUST_ADD_TENANT_ID(400445, "租户ID不存在");


    OAuth2Error(Integer status, String error) {
        this.status = status;
        this.error = error;
    }

    /**
     * 状态信息
     */
    private final Integer status;

    /**
     * 错误消息
     */
    private final String error;

}
