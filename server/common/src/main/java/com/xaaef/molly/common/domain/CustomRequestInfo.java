package com.xaaef.molly.common.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


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
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CustomRequestInfo implements java.io.Serializable {

    /**
     * 请求方式  GET POST PUT DELETE
     */
    @Schema(description = "请求方式")
    private String method;

    /**
     * 请求 url
     */
    @Schema(description = "请求url")
    private String requestUrl;

    /**
     * 请求的ip地址
     */
    @Schema(description = "ip地址")
    private String ip;

    /**
     * 操作系统类型
     */
    @Schema(description = "操作系统")
    private String osName;

    /**
     * 浏览器
     */
    @Schema(description = "浏览器")
    private String browser;

    /**
     * 地址
     */
    @Schema(description = "真实地址")
    private String address;

}
