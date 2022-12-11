package com.xaaef.molly.core.auth.po;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.NotNull;

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
public class LoginFormPO {

    @NotNull(message = "用户名不能为空")
    @Length(min = 5, message = "用户名长度不能少于5位")
    private String username;

    @NotNull(message = "密码不能为空")
    @Length(min = 5, message = "密码长度不能少于5位")
    private String password;

    @NotNull(message = "验证码不能为空")
    @Length(min = 5, max = 5, message = "验证码长度是5位")
    private String codeText;

    @NotNull(message = "验证码 KEY 不能为空")
    @Length(min = 5, message = "验证码 KEY长度不能少于16位")
    private String codeKey;


}
