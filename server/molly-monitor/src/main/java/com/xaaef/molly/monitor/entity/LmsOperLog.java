package com.xaaef.molly.monitor.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 操作日志
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/2/6 15:27
 */

@TableName("lms_oper_log")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LmsOperLog implements java.io.Serializable {

    /**
     * ID
     */
    @TableId
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
     * 用户ID
     */
    private Long userId;

    /**
     * 用户昵称
     */
    @TableField(exist = false)
    private String nickname;

    /**
     * 用户名
     */
    @TableField(exist = false)
    private String username;

    /**
     * 头像
     */
    @TableField(exist = false)
    private String avatar;

    /**
     * 方法
     */
    private String method;

    /**
     * 方法参数
     */
    private String methodArgs;

    /**
     * 请求类型
     */
    private String requestMethod;

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 请求IP
     */
    private String requestIp;

    /**
     * 请求地址
     */
    private String address;

    /**
     * 请求响应
     */
    private String responseResult;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 错误日志
     */
    private String errorLog;

    /**
     * 耗时(毫秒)
     */
    private Long timeCost;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

}
