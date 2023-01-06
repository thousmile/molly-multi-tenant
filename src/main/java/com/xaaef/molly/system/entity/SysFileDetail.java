package com.xaaef.molly.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * [ 通用 ] 文件记录表
 *
 * @TableName sys_file_detail
 */

@TableName(value = "sys_file_detail")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysFileDetail implements java.io.Serializable {

    /**
     * 文件id
     */
    @TableId
    private String id;

    /**
     * 文件访问地址
     */
    private String url;

    /**
     * 文件大小，单位字节
     */
    private Long size;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 原始文件名
     */
    private String originalFilename;

    /**
     * 基础存储路径
     */
    private String basePath;

    /**
     * 存储路径
     */
    private String path;

    /**
     * 文件扩展名
     */
    private String ext;

    /**
     * MIME类型
     */
    private String contentType;

    /**
     * 存储平台
     */
    private String platform;

    /**
     * 缩略图访问路径
     */
    private String thUrl;

    /**
     * 缩略图名称
     */
    private String thFilename;

    /**
     * 缩略图大小，单位字节
     */
    private Long thSize;

    /**
     * 缩略图MIME类型
     */
    private String thContentType;

    /**
     * 文件所属对象id
     */
    private String objectId;

    /**
     * 文件所属对象类型，例如用户头像，评价图片
     */
    private String objectType;

    /**
     * 附加属性
     */
    private String attr;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}