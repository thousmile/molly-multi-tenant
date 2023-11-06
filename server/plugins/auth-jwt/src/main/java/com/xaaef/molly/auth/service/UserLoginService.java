package com.xaaef.molly.auth.service;

import com.xaaef.molly.auth.exception.JwtAuthException;
import com.xaaef.molly.auth.jwt.JwtLoginUser;
import com.xaaef.molly.auth.jwt.JwtTokenValue;
import com.xaaef.molly.auth.po.LoginFormPO;
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
     * @param po
     * @param request
     * @return TokenValue 值
     * @throws AuthenticationException
     */
    JwtTokenValue login(LoginFormPO po, HttpServletRequest request) throws JwtAuthException;


    /**
     * 刷新内存中用户的权限
     */
    void refreshAuthoritys();


    /**
     * 刷新内存中用户的权限
     */
    void refreshAuthoritys(JwtLoginUser loginUser);


}
