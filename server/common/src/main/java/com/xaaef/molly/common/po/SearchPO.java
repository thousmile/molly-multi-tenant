package com.xaaef.molly.common.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
public class SearchPO extends PagePO implements java.io.Serializable {

    /**
     * 搜索，关键字
     */
    @Schema(description = "关键字搜索")
    private String keywords;

}
