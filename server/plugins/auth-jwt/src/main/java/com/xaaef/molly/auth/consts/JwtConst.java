package com.xaaef.molly.auth.consts;


import static com.xaaef.molly.common.consts.WebSocketKey.STOMP_ENDPOINT;

public class JwtConst {

    // 用户登录的地址
    public static final String LOGIN_URL = "/auth/login";

    // 用户登出的地址
    public static final String LOGOUT_URL = "/auth/logout";

    public static final String LOGIN_USER_URL = "/auth/login/user";

    // 刷新token
    public static final String REFRESH_URL = "/auth/refresh";

    // 验证码
    public static final String CAPTCHA_CODES_URL = "/auth/captcha/codes";

    // 不需要认证的路径
    public static final String[] WHITE_LIST = {
            LOGIN_URL,
            String.format("%s/**", CAPTCHA_CODES_URL),
            String.format("%s/**", STOMP_ENDPOINT),
            "/actuator/**",
            "/v2/api-docs",
            "/v3/api-docs/**",
            "/doc.html",
            "/configuration/ui",
            "/swagger-resources",
            "/configuration/security",
            "/webjars/**",
            "/swagger-resources/configuration/ui",
            "/swagger-ui.html",
            "/error",
            "/error/**"
    };

}
