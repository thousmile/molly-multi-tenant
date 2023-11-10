package com.xaaef.molly.system.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * 文件上传token。给前端使用，前端直接上传到 oss 服务器。无需经过后端
 *
 * @author WangChenChen
 * @version 2.0
 * @date 2023/11/10 17:59
 */

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadTokenVO {

    @Schema(description = "上传凭证")
    private String token;

    @Schema(description = "OSS服务提供商")
    private String platform;

    @Schema(description = "桶名称")
    private String bucketName;

    @Schema(description = "自定义域名")
    private String domain;

    @Schema(description = "前缀路径")
    private String basePath;

}
