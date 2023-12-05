package com.xaaef.molly.common.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;


@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ValueRange<T> implements java.io.Serializable {

    @Schema(description = "开始")
    private T begin;

    @Schema(description = "结束")
    private T end;

}
