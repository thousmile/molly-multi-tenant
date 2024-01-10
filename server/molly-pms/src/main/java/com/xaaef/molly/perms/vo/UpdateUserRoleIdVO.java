package com.xaaef.molly.perms.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * <p>
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/9 17:46
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateUserRoleIdVO {


    @Schema(description = "用户ID不能为空！", requiredMode = REQUIRED)
    @NotNull(message = "用户ID不能为空!")
    private Long userId;


    @Schema(description = "角色Id列表！", requiredMode = REQUIRED)
    @NotNull(message = "角色Id列表!")
    @Size(min = 1, message = "角色列表,最少选择1个!")
    private Set<Long> roles;

}
