package com.xaaef.molly.system.po;

import com.xaaef.molly.system.entity.SysTemplate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Set;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/13 16:07
 */

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用于创建租户")
public class CreateTenantPO implements java.io.Serializable {

    @Schema(description = "租户唯一ID！不填写就随机生成")
    private String tenantId;

    @Schema(description = "租户名称必须填写！", requiredMode = REQUIRED)
    @NotBlank(message = "租户名称必须填写！")
    private String name;

    @Schema(description = "租户邮箱必须填写！", requiredMode = REQUIRED)
    @NotBlank(message = "租户邮箱必须填写！")
    @Email(message = "邮箱格式错误！")
    private String email;

    @Schema(description = "租户联系人必须填写！", requiredMode = REQUIRED)
    @NotBlank(message = "租户联系人必须填写！")
    private String linkman;

    @Schema(description = "联系方式必须填写！支持手机和座机", requiredMode = REQUIRED)
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$|^0\\d{2,3}-?\\d{7,8}$", message = "联系方式格式错误,支持手机和座机！")
    private String contactNumber;

    @Schema(description = "租户logo, 如果不填写会使用默认logo", requiredMode = REQUIRED)
    private String logo;

    @Schema(description = "租户所在地编号！", requiredMode = REQUIRED)
    @NotNull(message = "租户所在地编号必须填写！")
    private Long areaCode;

    @Schema(description = "租户详细地址！", requiredMode = REQUIRED)
    @NotBlank(message = "租户详细地址必须填写！")
    private String address;

    @Schema(description = "租户权限模板Id！", requiredMode = REQUIRED)
    @NotNull(message = "租户权限模板必须填写！")
    @Size(min = 1, message = "租户权限模板！最少选择一个！")
    private Set<SysTemplate> templates;

    @Schema(description = "过期时间,如果不填写，默认是 10 年！")
    private LocalDateTime expired;



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
