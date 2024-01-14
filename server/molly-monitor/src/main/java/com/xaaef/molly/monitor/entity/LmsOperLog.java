package com.xaaef.molly.monitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "用户操作日志")
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
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "ID")
    private String id;

    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;

    /**
     * 服务名称
     */
    @Schema(description = "服务名称")
    private String serviceName;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称")
    @TableField(exist = false)
    private String nickname;

    /**
     * 用户名
     */
    @Schema(description = "用户昵称")
    @TableField(exist = false)
    private String username;

    /**
     * 头像
     */
    @Schema(description = "用户昵称")
    @TableField(exist = false)
    private String avatar;

    /**
     * 方法
     */
    @Schema(description = "方法名")
    private String method;

    /**
     * 方法参数
     */
    @Schema(description = "方法参数")
    private String methodArgs;

    /**
     * 请求类型
     */
    @Schema(description = "http请求类型")
    private String requestMethod;

    /**
     * 请求url
     */
    @Schema(description = "请求url")
    private String requestUrl;

    /**
     * 请求IP
     */
    @Schema(description = "请求IP")
    private String requestIp;

    /**
     * 请求地址
     */
    @Schema(description = "请求地址")
    private String address;

    /**
     * 请求响应
     */
    @Schema(description = "用户昵称")
    private String responseResult;

    /**
     * 状态
     */
    @Schema(description = "结果状态")
    private Integer status;

    /**
     * 错误日志
     */
    @Schema(description = "错误日志")
    private String errorLog;

    /**
     * 耗时(毫秒)
     */
    @Schema(description = "耗时(毫秒)")
    private Long timeCost;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

}
