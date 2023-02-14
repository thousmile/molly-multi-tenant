package com.xaaef.molly.perms.vo;

import cn.hutool.core.lang.tree.Tree;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/1/6 13:57
 */


@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "用户权限！")
public class UserRightsVO implements java.io.Serializable {

    @Schema(description = "按钮权限！")
    private Set<ButtonVO> buttons;

    @Schema(description = "菜单权限！")
    private List<Tree<Long>> menus;

}
