package com.xaaef.molly.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * <p>
 * 修改租户模板
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 12:14
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateTenantTemplateIdVO {

    @Schema(description = "租户ID不能为空！")
    @NotEmpty(message = "租户ID不能为空!")
    private String tenantId;

    @Schema(description = "租户权限模板！最少选择一个！")
    @NotNull(message = "租户权限模板必须填写！")
    @Size(min = 1, message = "租户权限模板！最少选择一个！")
    private Set<Long> templateIds;


}
