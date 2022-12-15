package com.xaaef.molly.perms.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 修改密码
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
public class UpdatePasswordVO {

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID不能为空！")
    @NotNull(message = "用户ID不能为空!")
    private Long userId;

    /**
     * 旧密码
     */
    @ApiModelProperty(value = "旧密码长度要大于6位！")
    @NotNull(message = "旧密码不能为空!")
    @Length(min = 5, message = "旧密码长度要大于6位!")
    private String oldPwd;

    /**
     * 新密码
     */
    @ApiModelProperty(value = "新密码长度要大于6位！")
    @NotNull(message = "新密码不能为空!")
    @Length(min = 5, message = "新密码长度要大于6位!")
    private String newPwd;

    /**
     * 确认密码
     */
    @ApiModelProperty(value = "确认密码长度要大于6位！")
    @NotNull(message = "确认密码不能为空!")
    @Length(min = 5, message = "确认密码长度要大于6位!")
    private String confirmPwd;

}
