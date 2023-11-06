package com.xaaef.molly.auth.service;

import com.xaaef.molly.auth.exception.JwtAuthException;
import com.xaaef.molly.auth.jwt.JwtLoginUser;
import com.xaaef.molly.auth.jwt.JwtTokenProperties;
import com.xaaef.molly.auth.jwt.JwtTokenValue;

import java.util.Map;
import java.util.Set;


/**
 * <p>
 * 服务端 token 认证
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/12 16:28
 */


public interface JwtTokenService {

    /**
     * 设置登录的用户 到redis中
     *
     * @param loginUser
     */
    void setLoginUser(JwtLoginUser loginUser);


    /**
     * 修改登录的用户 到redis中
     *
     * @param loginUser
     */
    void updateLoginUser(JwtLoginUser loginUser);


    /**
     * 根据 loginId 获取 用户信息
     * <p>
     * 例: login_tokens:c6ea9a4cbb0e4fd093764c87fcfb372f
     *
     * @param loginId
     */
    JwtLoginUser getLoginUser(String loginId);


    /**
     * 根据 用户名 获取 用户信息
     *
     * @return String 用户名称
     * @author Wang Chen Chen
     * @date 2021/7/12 16:29
     */
    JwtLoginUser getLoginUserByUsername(String username);


    /**
     * 校验 token 值是否正确
     * <p>
     * 例: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdG6IjE2NDPuFA
     *
     * @param bearerToken
     * @author Wang Chen Chen
     * @date 2021/7/12 16:29
     */
    JwtLoginUser validate(String bearerToken) throws JwtAuthException;


    /**
     * 刷新 token
     */
    JwtTokenValue refresh();


    /**
     * 退出登录
     */
    void logout();


    /**
     * 创建 jwt 字符串
     */
    String createJwtStr(String id);


    /**
     * 获取 当前租户，在线的用户信息
     *
     * @return String 用户名称
     * @author Wang Chen Chen
     * @date 2021/7/12 16:29
     */
    Set<String> listLoginUsername();


    /**
     * 获取 所有的在线的用户名
     *
     * @return String 用户名称
     * @author Wang Chen Chen
     * @date 2021/7/12 16:29
     */
    Set<String> listLoginIds();


    /**
     * 获取 当前租户，在线的 用户信息
     * key: 用户ID
     * value: 用户信息
     *
     * @return String 用户名称
     * @author Wang Chen Chen
     * @date 2021/7/12 16:29
     */
    Map<Long, JwtLoginUser> mapLoginUser();


    /**
     * 获取 所有租户 在线的 用户信息
     * key: 用户ID
     * value: 用户信息
     *
     * @return String 用户名称
     * @author Wang Chen Chen
     * @date 2021/7/12 16:29
     */
    Map<Long, JwtLoginUser> mapAllLoginUser();


    /**
     * 获取 jwt 配置
     *
     * @author Wang Chen Chen
     * @date 2021/7/12 16:29
     */
    JwtTokenProperties getProps();


}
