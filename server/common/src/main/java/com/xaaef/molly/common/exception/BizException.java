package com.xaaef.molly.common.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <p>
 * 业务 异常
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2024/04/21 9:31
 */

@Getter
@Setter
@ToString
public class BizException extends RuntimeException {

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

}
