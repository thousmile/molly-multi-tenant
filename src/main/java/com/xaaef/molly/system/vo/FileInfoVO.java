package com.xaaef.molly.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/1/6 18:11
 */

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoVO implements java.io.Serializable {

    /**
     * 文件访问地址
     */
    @Schema(description = "文件访问地址！")
    private String url;

    /**
     * 文件名称
     */
    @Schema(description = "文件名称！")
    private String filename;

    /**
     * 文件扩展名
     */
    @Schema(description = "文件扩展名！")
    private String ext;

}
