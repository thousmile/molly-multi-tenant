package com.xaaef.molly.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * <p>
 * 请求日志
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
public class CustomRequestInfo implements java.io.Serializable {

    /**
     * 请求方式  GET POST PUT DELETE
     */
    private String method;

    /**
     * 请求 url
     */
    private String requestUrl;

    /**
     * 请求的ip地址
     */
    private String ip;

    /**
     * 操作系统类型
     */
    private String osName;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 地址
     */
    private String address;

}
