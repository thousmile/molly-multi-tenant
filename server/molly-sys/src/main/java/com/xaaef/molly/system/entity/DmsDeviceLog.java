package com.xaaef.molly.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Map;

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
@Document(indexName = "#{T(com.xaaef.molly.common.consts.ESIndexName).getDeviceLogIndex()}", createIndex = false)
public class DmsDeviceLog implements java.io.Serializable {

    /**
     * 唯一 ID
     */
    @Id
    @Field(type = FieldType.Text)
    private String id;

    /**
     * 设备 ID
     */
    @Field(type = FieldType.Long)
    private Long deviceId;

    /**
     * 设备 SN
     */
    @Field(type = FieldType.Text)
    private String serialNumber;

    /**
     * 产品 ID
     */
    @Field(type = FieldType.Text)
    private String productId;

    /**
     * 区域 ID
     */
    @Field(type = FieldType.Long)
    private Long areaId;


    /**
     * 消息主题
     */
    @Field(type = FieldType.Text)
    private String topic;

    /**
     * 日志类型
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String logType;

    /**
     * 日志内容
     */
    @Field(type = FieldType.Object)
    private Map<String, Object> content;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    protected LocalDateTime createTime;

    /**
     * 索引名称
     */
    @Transient
    @JsonIgnore
    private String indexName;

}
