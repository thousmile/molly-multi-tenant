package com.xaaef.molly.common.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkedTarget implements Serializable {

    @Schema(description = "目标Id")
    private Long targetId;

    @Schema(description = "来源Id")
    private Long sourceId;

}
