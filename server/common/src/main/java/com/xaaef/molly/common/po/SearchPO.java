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
@Schema(description = "搜索分页查询")
public class SearchPO extends PagePO implements java.io.Serializable {

    /**
     * 搜索，关键字
     */
    @Schema(description = "关键字搜索")
    private String keywords;

    /**
     * 包含创建和修改用户信息
     * include create and update user
     */
    @Schema(description = "包含创建和修改用户信息")
    private Boolean includeCauu;

    public boolean isIncludeCauu() {
        return this.getIncludeCauu() != null && this.getIncludeCauu();
    }

}
