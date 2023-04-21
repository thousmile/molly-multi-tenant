package com.xaaef.molly.monitor.entity;

import com.xaaef.molly.tenant.util.TenantUtils;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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

@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "#{@indexNameGenerator.getTenantId()+'_lms_oper_log'}")
public class LmsOperLog implements java.io.Serializable {
    /**
     * ID
     */
    @Id
    @Field(type = FieldType.Text)
    private String id;

    /**
     * 标题
     */
    @Field(type = FieldType.Text)
    private String title;

    /**
     * 描述
     */
    @Field(type = FieldType.Text)
    private String description;

    /**
     * 服务名称
     */
    @Field(type = FieldType.Text)
    private String serviceName;

    /**
     * 用户ID
     */
    @Field(type = FieldType.Long)
    private Long userId;

    /**
     * 用户昵称
     */
    @Transient
    private String nickname;

    /**
     * 用户名
     */
    @Transient
    private String username;

    /**
     * 头像
     */
    @Transient
    private String avatar;

    /**
     * 方法
     */
    @Field(type = FieldType.Text)
    private String method;

    /**
     * 方法参数
     */
    @Field(type = FieldType.Text)
    private String methodArgs;

    /**
     * 请求类型
     */
    @Field(type = FieldType.Text)
    private String requestMethod;

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
     * 请求响应
     */
    @Field(type = FieldType.Text)
    private String responseResult;

    /**
     * 状态
     */
    @Field(type = FieldType.Integer)
    private Integer status;

    /**
     * 错误日志
     */
    @Field(type = FieldType.Text)
    private String errorLog;

    /**
     * 耗时(毫秒)
     */
    @Field(type = FieldType.Long)
    private Long timeCost;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    protected LocalDateTime createTime;

}
