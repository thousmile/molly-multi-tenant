package com.xaaef.molly.monitor.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 登录日志
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/2/6 15:27
 */

@Schema(description = "用户登录日志")
@TableName("lms_login_log")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LmsLoginLog implements java.io.Serializable {

    /**
     * ID
     */
    @TableId
    @Schema(description = "ID")
    private String id;

    /**
     * 授权类型
     */
    @Schema(description = "授权类型")
    private String grantType;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称")
    private String nickname;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 头像
     */
    @Schema(description = "用户头像")
    private String avatar;

    /**
     * 请求地址
     */
    @Schema(description = "请求URL")
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
     * 操作系统
     */
    @Schema(description = "操作系统")
    private String osName;

    /**
     * 浏览器
     */
    @Schema(description = "浏览器")
    private String browser;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    /**
     * 租户ID
     */
    @TableField(exist = false)
    @JsonIgnore
    private String tenantId;

}
