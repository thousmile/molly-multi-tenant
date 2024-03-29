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
@Schema(description = "上级ID, 搜索分页查询")
public class SearchParentPO extends SearchPO implements java.io.Serializable {

    @Schema(description = "树节点 上级ID")
    private Long parentId;

}
