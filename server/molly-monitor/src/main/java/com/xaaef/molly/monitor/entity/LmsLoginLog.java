package com.xaaef.molly.monitor.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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

@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "#{@indexNameGenerator.getTenantId()+'_lms_login_log'}")
public class LmsLoginLog implements java.io.Serializable {

    /**
     * ID
     */
    @Id
    @Field(type = FieldType.Text)
    private String id;

    /**
     * 授权类型
     */
    @Field(type = FieldType.Text)
    private String grantType;

    /**
     * 用户ID
     */
    @Field(type = FieldType.Long)
    private Long userId;

    /**
     * 用户昵称
     */
    @Field(type = FieldType.Text)
    private String nickname;

    /**
     * 用户名
     */
    @Field(type = FieldType.Text)
    private String username;

    /**
     * 头像
     */
    @Field(type = FieldType.Text)
    private String avatar;

    /**
     * 请求地址
     */
    @Field(type = FieldType.Text)
    private String requestUrl;

    /**
     * 请求IP
     */
    @Field(type = FieldType.Text)
    private String requestIp;

    /**
     * 请求地址
     */
    @Field(type = FieldType.Text)
    private String address;

    /**
     * 操作系统
     */
    @Field(type = FieldType.Text)
    private String osName;

    /**
     * 浏览器
     */
    @Field(type = FieldType.Text)
    private String browser;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    protected LocalDateTime createTime;

}
