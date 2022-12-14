package com.xaaef.molly.system.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * <p>
 * 角色，部门，租户模板 等... 关联菜单
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/23 15:14
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BindingMenusVO implements java.io.Serializable {

    @NotNull(message = "ID不能为空!")
    private Long id;

    @NotNull(message = "菜单不能为空!")
    @Size(min = 1, message = "菜单最少是一个!")
    private Set<Long> menus;

}
