package com.xaaef.molly.perms.po;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;


/**
 * <p>
 * 用户登录，参数传递
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 2.0
 * @date 2019/4/18 11:45
 */


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserVO implements java.io.Serializable {

    @ApiModelProperty(value = "用户名")
    @NotNull(message = "用户名不能为空")
    @Length(min = 5, message = "用户名长度不能少于五位")
    private String username;

    @ApiModelProperty(value = "用户密码")
    @NotNull(message = "密码不能为空")
    @Length(min = 5, message = "密码长度不能少于五位")
    private String password;

    @ApiModelProperty(value = "验证码")
    @NotNull(message = "验证码不能为空")
    @Length(min = 4, max = 4, message = "验证码长度是四位")
    private String codeText;

    @ApiModelProperty(value = "验证码 KEY")
    @NotNull(message = "验证码 KEY 不能为空")
    private String codeKey;

}
