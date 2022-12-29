package com.xaaef.molly.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

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

    @Schema(description = "ID不能为空！", requiredMode = REQUIRED)
    @NotNull(message = "ID不能为空!")
    private Long id;

    @Schema(description = "菜单最少是一个", requiredMode = REQUIRED)
    @NotNull(message = "菜单不能为空!")
    @Size(min = 1, message = "菜单最少是一个!")
    private Set<Long> menus;

}
