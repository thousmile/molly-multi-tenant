package com.xaaef.molly.common.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 矩形 点位
 * </p>
 *
 * @author WangChenChen
 * @version 1.2
 * @date 2022/7/22 10:12
 */


@Data
public class RectPoint<T> {

    /**
     * x 坐标
     */
    @Schema(description = "x坐标")
    private Double x;

    /**
     * y 坐标
     */
    @Schema(description = "y坐标")
    private Double y;

    /**
     * 唯一ID
     */
    private T id;

    public RectPoint() {
    }

    public RectPoint(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public RectPoint(Double x, Double y, T id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }
}
