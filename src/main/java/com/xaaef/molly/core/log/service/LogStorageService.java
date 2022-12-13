package com.xaaef.molly.core.log.service;

import com.xaaef.molly.common.domain.CustomRequestInfo;
import com.xaaef.molly.core.auth.jwt.JwtLoginUser;
import org.aspectj.lang.JoinPoint;

/**
 * <p>
 * 运行 日志 存储
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/8/11 15:29
 */

public interface LogStorageService {


    /**
     * 异步保存 登录日志 到 ES 中
     *
     * @param loginUser
     * @author Wang Chen Chen
     * @date 2021/8/11 15:30
     */
    void asyncLoginSave(JwtLoginUser loginUser, CustomRequestInfo request);


    /**
     * 异步保存 操作日志 到 ES 中
     *
     * @param operLog
     * @author Wang Chen Chen
     * @date 2021/8/11 15:30
     */
    void asyncOperateSave(JoinPoint joinPoint, Object resp,
                          Throwable e, long timeCost,
                          CustomRequestInfo request);


}
