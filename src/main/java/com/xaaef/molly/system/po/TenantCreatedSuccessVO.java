package com.xaaef.molly.system.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * All rights Reserved, Designed By 深圳市铭灏天智能照明设备有限公司
 * <p>
 * 创建租户
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/16 12:02
 * @copyright 2021 http://www.mhtled.com Inc. All rights reserved.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TenantCreatedSuccessVO implements java.io.Serializable {

    @ApiModelProperty(value = "管理员名称！")
    private String adminNickname;

    @ApiModelProperty(value = "管理员登录用户名")
    private String adminUsername;

    @ApiModelProperty(value = "管理员手机号")
    private String adminMobile;

    @ApiModelProperty(value = "管理员密码")
    private String adminPassword;

}
