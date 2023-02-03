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


    @Schema(description = "管理员名称！如果不填写，默认是商户名称！")
    private String adminNickname;

    @Schema(description = "管理员登录用户名,如果不填写，随机生成！")
    private String adminUsername;

    @Schema(description = "管理员手机号,如果不填写，默认使用商户联系人手机号！")
    private String adminMobile;

    @Schema(description = "管理员邮箱,如果不填写，默认使用商户邮箱！")
    private String adminEmail;

    @Schema(description = "管理员密码,如果不填写，默认使用默认密码！")
    private String adminPwd;

}
