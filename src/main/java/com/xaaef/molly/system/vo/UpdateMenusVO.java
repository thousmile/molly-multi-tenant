package com.xaaef.molly.system.vo;

import cn.hutool.core.lang.tree.Tree;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色，部门，租户模板 等... 关联菜单
 * </p>
 *
 * @author Wang Chen Chen<932560435@qq.com>
 * @version 1.0.1
 * @date 2021/7/24 14:39
 */

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMenusVO implements java.io.Serializable {

    /**
     * 已经拥有的菜单 ID
     */
    @Schema(description = "拥有的菜单ID！")
    private Set<Long> have;

    /**
     * 全部 菜单 ID 和 名称
     */
    @Schema(description = "全部 菜单ID和名称！")
    private List<Tree<Long>> all;

}


