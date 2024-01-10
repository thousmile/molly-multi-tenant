package com.xaaef.molly.corems.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

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
@Schema(description = "重置密码！")
public class ResetPasswordVO {

    /**
     * 项目ID
     */
    @Schema(description = "项目ID不能为空！", requiredMode = REQUIRED)
    @NotNull(message = "项目ID不能为空!")
    private Long projectId;

    /**
     * 新密码
     */
    @Schema(description = "新密码长度要大于5位！", requiredMode = REQUIRED)
    @NotNull(message = "新密码不能为空!")
    @Length(min = 5, message = "新密码长度要大于5位!")
    private String newPwd;

}
