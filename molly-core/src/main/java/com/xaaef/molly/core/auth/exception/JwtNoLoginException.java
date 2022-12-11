package com.xaaef.molly.core.auth.exception;

import com.xaaef.molly.core.auth.enums.OAuth2Error;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.AuthenticationException;

import static com.xaaef.molly.core.auth.enums.OAuth2Error.OAUTH2_EXCEPTION;


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
