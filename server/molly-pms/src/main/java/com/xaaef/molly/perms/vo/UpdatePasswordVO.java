package com.xaaef.molly.perms.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

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
    @Schema(description = "用户ID不能为空！", requiredMode = REQUIRED)
    @NotNull(message = "用户ID不能为空!")
    private Long userId;

    /**
     * 旧密码
     */
    @Schema(description = "旧密码长度要大于5位！", requiredMode = REQUIRED)
    @NotNull(message = "旧密码不能为空!")
    @Length(min = 5, message = "旧密码长度要大于5位!")
    private String oldPwd;

    /**
     * 新密码
     */
    @Schema(description = "新密码长度要大于5位！", requiredMode = REQUIRED)
    @NotNull(message = "新密码不能为空!")
    @Length(min = 5, message = "新密码长度要大于5位!")
    private String newPwd;

    /**
     * 确认密码
     */
    @Schema(description = "确认密码长度要大于5位！", requiredMode = REQUIRED)
    @NotNull(message = "确认密码不能为空!")
    @Length(min = 5, message = "确认密码长度要大于5位!")
    private String confirmPwd;

}
