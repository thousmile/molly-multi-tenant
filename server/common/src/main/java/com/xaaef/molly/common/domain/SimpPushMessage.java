package com.xaaef.molly.common.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "推送消息")
public class SimpPushMessage implements java.io.Serializable {

    @Schema(description = "Id")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "状态 0.失败  1.成功 ")
    private Byte status;

}
