package com.xaaef.molly.internal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * <p>
 * 登录日志
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/12 12:08
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginLogDTO implements java.io.Serializable {

    /**
     * 消息ID
     */
    private String id;

    /**
     * 认证授权方式
     */
    private String grantType;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 操作人员
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户名
     */
    private String username;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 请求 url
     */
    private String requestUrl;

    /**
     * 请求的ip地址
     */
    private String requestIp;

    /**
     * 操作地点
     */
    private String address;


    /**
     * 操作系统类型
     */
    private String osName;


    /**
     * 浏览器
     */
    private String browser;


    /**
     * 操作时间
     */
    private LocalDateTime createTime;

}
