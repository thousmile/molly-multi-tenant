package com.xaaef.molly.system.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/13 16:07
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "图片大小")
public class ImageSizePO implements java.io.Serializable {

    @Schema(description = "宽度")
    private Integer width;

    @Schema(description = "高度")
    private Integer height;

}
