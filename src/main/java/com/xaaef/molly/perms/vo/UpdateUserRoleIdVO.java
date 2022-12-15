package com.xaaef.molly.perms.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

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


    @ApiModelProperty(value = "用户ID不能为空！")
    @NotNull(message = "用户ID不能为空!")
    private Long userId;


    @ApiModelProperty(value = "角色最少是一个！")
    @NotNull(message = "角色不能为空!")
    @Size(min = 1, message = "角色最少是一个!")
    private Set<Long> roles;

}
