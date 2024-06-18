package com.xaaef.molly.auth.jwt;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * </p>
 *
 * @author Wang Chen Chen<932560435@qq.com>
 * @version 1.0.0
 * @date 2020/7/2515:30
 */

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "jwt.token")
public class JwtTokenProperties {

    /**
     * 请求头的KEY。默认 "Authorization"
     */
    private String tokenHeader = "Authorization";

    /**
     * token 类型
     */
    private String tokenType = "Bearer ";

    /**
     * token 缓存 过期时间  单位(秒)
     */
    private Integer tokenExpired = 7200;

    /**
     * 短信验证码过期时间 单位(秒)
     */
    private Integer smsCodeExpired = 600;

    /**
     * 用户被挤下线，提示的过期时间 单位(秒)
     */
    private Integer promptExpired = 600;

    /**
     * 验证码 过期时间 单位(秒)
     */
    private Integer captchaExpired = 180;

    /**
     * 秘钥
     */
    private String secret = "2N321lIkh$*!IfNt4&5!YZykD$7@ApaM8r@b@r@&4CZ7eqKe!s";

    /**
     * 私钥
     * openssl genrsa -out rsa_private_key.pem 1024
     * openssl genrsa -out rsa_private_key.pem 2048
     */
    private String rsaPrivateKey = "classpath:pem/rsa_private_key.pem";

    /**
     * 公钥
     * openssl rsa -in rsa_private_key.pem -pubout -out rsa_public_key.pem
     */
    private String rsaPublicKey = "classpath:pem/rsa_public_key.pem";

    /**
     * 单点登录，是否启用
     */
    private Boolean sso = Boolean.TRUE;

    /**
     * 需要排除的URL
     */
    private String[] excludePath = {
            "/open/**"
    };

}
