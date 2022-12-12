package com.xaaef.molly.common.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

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
public class PagePO implements java.io.Serializable {

    public PagePO() {
        this.pageIndex = 1;
        this.pageSize = 10;
    }

    /**
     * 当前第几页
     */
    private Integer pageIndex;

    /**
     * 每页多少条数据
     */
    private Integer pageSize;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 结束日期
     */
    private LocalDate endDate;

    public Integer getPageIndex() {
        if (pageIndex == null || pageIndex <= 1) {
            return 0;
        } else {
            return pageIndex - 1;
        }
    }

}
