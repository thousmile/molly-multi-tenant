package com.xaaef.molly.common.consts;

public class LoginConst {

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 在线用户，令牌前缀
     */
    public static final String ONLINE_USER_KEY = "online_user:";

    /**
     * 强制下线，令牌前缀
     */
    public static final String FORCED_OFFLINE_KEY = "forced_offline_user:";

    /**
     * 防止重复提交 令牌前缀
     */
    public static final String NO_REPEAT_SUBMIT = "no_repeat_submit";

}
