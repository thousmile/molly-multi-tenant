package com.xaaef.molly.core.log.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 操作日志记录表 oper_log
 *
 * @author wangwei
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperLog implements java.io.Serializable {

    /**
     * 消息ID
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 操作人员
     */
    private Long userId;

    /**
     * 操作人员
     */
    private String nickname;

    /**
     * 方法名称
     */
    private String method;

    /**
     * 方法参数
     */
    private List<Map<String, Object>> methodArgs;

    /**
     * 请求方式
     */
    private String requestMethod;

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
     * 响应结果
     */
    private Map<String, Object> responseResult;

    /**
     * 操作状态（ 200.正常 其他.异常 ）
     */
    private Integer status;

    /**
     * 错误日志
     */
    private String errorLog;

    /**
     * 耗时
     */
    private Long timeCost;

    /**
     * 操作时间
     */
    private LocalDateTime createTime;

}
