package com.xaaef.molly.core.auth.service;

import com.xaaef.molly.core.auth.exception.JwtAuthException;
import com.xaaef.molly.core.auth.jwt.JwtTokenValue;
import com.xaaef.molly.core.auth.po.LoginFormPO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.AuthenticationException;


/**
 * <p>
 * 用户 登录 Service 接口
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 2.0
 * @date 2019/4/18 11:45
 */


public interface UserLoginService {

    /**
     * 用户登录
     *
     * @param user
     * @return String token 值
     * @throws AuthenticationException
     */
    JwtTokenValue login(LoginFormPO po, HttpServletRequest request) throws JwtAuthException;

}
