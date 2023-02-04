package com.xaaef.molly.core.auth.po;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

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

    @Schema(description = "用户名", requiredMode = REQUIRED)
    @NotNull(message = "用户名不能为空")
    @Length(min = 5, message = "用户名长度不能少于5位")
    private String username;

    @Schema(description = "密码", requiredMode = REQUIRED)
    @NotNull(message = "密码不能为空")
    @Length(min = 5, message = "密码长度不能少于5位")
    private String password;

    @Schema(description = "验证码", requiredMode = REQUIRED)
    @NotNull(message = "验证码不能为空")
    @Length(min = 4, max = 4, message = "验证码长度是4位")
    private String codeText;

    @Schema(description = "验证码 KEY", requiredMode = REQUIRED)
    @NotNull(message = "验证码 KEY 不能为空")
    @Length(min = 5, message = "验证码 KEY长度不能少于16位")
    private String codeKey;


}
