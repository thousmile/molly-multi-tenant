package com.xaaef.molly.perms.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 重置密码
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
public class ResetPasswordVO {

    /**
     * 用户ID
     *
     * @date 2021/5/21 15:43
     */
    @ApiModelProperty(value = "用户ID不能为空！")
    @NotNull(message = "用户ID不能为空!")
    private Long userId;

    /**
     * 新密码
     *
     * @date 2021/5/21 15:43
     */
    @ApiModelProperty(value = "新密码长度要大于6位！")
    @NotNull(message = "新密码不能为空!")
    @Length(min = 5, message = "新密码长度要大于6位!")
    private String newPwd;

}
