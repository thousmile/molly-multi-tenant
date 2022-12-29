package com.xaaef.molly.system.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * <p>
 * 创建租户
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/16 12:02
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TenantCreatedSuccessVO implements java.io.Serializable {

    @Schema(description = "管理员名称！", requiredMode = REQUIRED)
    private String adminNickname;

    @Schema(description = "管理员登录用户名", requiredMode = REQUIRED)
    private String adminUsername;

    @Schema(description = "管理员手机号", requiredMode = REQUIRED)
    private String adminMobile;

    @Schema(description = "管理员密码", requiredMode = REQUIRED)
    private String adminPassword;

}
