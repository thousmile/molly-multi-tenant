package com.xaaef.molly.auth.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.AuthenticationException;


/**
 * <p>
 * 认证异常
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/5 9:31
 */

@Getter
@Setter
@ToString
public class JwtNoLoginException extends AuthenticationException {

    public JwtNoLoginException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
