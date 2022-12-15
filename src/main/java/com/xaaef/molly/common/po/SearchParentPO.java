package com.xaaef.molly.common.po;

import io.swagger.annotations.ApiModelProperty;
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
public class SearchParentPO extends SearchPO implements java.io.Serializable {

    @ApiModelProperty(value = "树节点 上级ID")
    private Long parentId;

}
