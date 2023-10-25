package com.xaaef.molly.internal.dto;

import lombok.*;
import lombok.experimental.Accessors;


/**
 * <p>
 * 租户
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */


@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class InitUserDTO implements java.io.Serializable {

    // 租户 ID
    private String tenantId;

    //  租户名称必须填写！
    private String name;

    //  租户名称必须填写！
    private String logo;

    // 管理员名称！如果不填写，默认是商户名称！
    private String adminNickname;

    // 管理员登录用户名,如果不填写，随机生成！
    private String adminUsername;

    // 管理员手机号,如果不填写，默认使用商户联系人手机号！
    private String adminMobile;

    // 管理员邮箱,如果不填写，默认使用商户邮箱！
    private String adminEmail;

    // 管理员密码,如果不填写，默认使用默认密码！
    private String adminPwd;

}
