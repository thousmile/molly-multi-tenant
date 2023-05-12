package com.xaaef.molly.common.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * <p>
 * 基础参数传递
 * </p>
 *
 * @author Wang Chen Chen<932560435@qq.com>
 * @date 2021/10/21 21:38
 */

@Getter
@Setter
@ToString
@Schema(description = "分页查询")
public class PagePO implements java.io.Serializable {

    public PagePO() {
        this.pageIndex = 1;
        this.pageSize = 10;
    }

    /**
     * 当前第几页
     */
    @Schema(description = "当前第几页", requiredMode = REQUIRED)
    private Integer pageIndex;

    /**
     * 每页多少条数据
     */
    @Schema(description = "每页多少条", requiredMode = REQUIRED)
    private Integer pageSize;

    /**
     * 开始日期
     */
    @Schema(description = "开始日期")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @Schema(description = "结束日期")
    private LocalDate endDate;

}
