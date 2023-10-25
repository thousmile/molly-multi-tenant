package com.xaaef.molly.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * <p>
 * 系统用户关联租户
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 12:14
 */

@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "系统用户授权多个租户")
public class UpdateUserTenantVO {

    @Schema(description = "用户ID不能为空！")
    @NotNull(message = "用户ID不能为空!")
    private Long userId;


    @Schema(description = "租户Id！最少选择一个！")
    @NotNull(message = "租户Id必须填写！")
    private Set<String> tenantIds;


}
