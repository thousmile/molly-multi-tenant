package com.xaaef.molly.perms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author WangChenChen
 * @version 1.2
 * @date 2022/6/24 16:33
 */


@Schema(description = "用户角色")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PmsRoleProxy extends PmsRole {

    @JsonIgnore
    @Schema(description = "用户ID")
    private Long userId;

}
