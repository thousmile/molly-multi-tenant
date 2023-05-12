package com.xaaef.molly.auth.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xaaef.molly.common.enums.AdminFlag;
import com.xaaef.molly.common.enums.StatusEnum;
import com.xaaef.molly.common.enums.UserType;
import com.xaaef.molly.auth.enums.GrantType;
import com.xaaef.molly.internal.dto.PmsRoleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

/**
 * <p>
 * spring security 认证 UserDetails 实现类
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 2.0
 * @date 2019/4/18 11:45
 */

@Schema(description = "登录的用户信息")
@ToString
@Getter
@Setter
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtLoginUser implements UserDetails, Principal {

    /**
     * 租户ID
     */
    @Schema(description = "租户ID")
    private String tenantId;

    /**
     * 登录ID
     */
    @Schema(description = "唯一登录ID")
    private String loginId;

    /**
     * 认证授权方式
     */
    @Schema(description = "授权方式")
    private GrantType grantType;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 头像
     */
    @Schema(description = "用户头像")
    private String avatar;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 手机号码
     */
    @Schema(description = "手机号码")
    private String mobile;

    /**
     * 邮箱
     */
    @Schema(description = "用户邮箱")
    private String email;

    /**
     * 昵称
     */
    @Schema(description = "用户昵称")
    private String nickname;

    /**
     * 密码
     */
    @Schema(description = "密码")
    @JsonIgnore
    private String password;

    /**
     * 用户状态， [ 0.禁用 1.正常 2.被删除 ]
     */
    @Schema(description = "用户状态， [ 0.禁用 1.正常 2.被删除 ]")
    private StatusEnum status;

    /**
     * 是否管理员 0. 普通用户  1. 管理员
     */
    @Schema(description = "是否管理员 0. 普通用户  1. 管理员")
    private AdminFlag adminFlag;

    /**
     * 用户类型 0. 租户用户  1. 系统用户
     */
    @Schema(description = "用户类型 0. 租户用户  1. 系统用户")
    private UserType userType;

    /**
     * 性别[ 0.女  1.男  2.未知]
     */
    @Schema(description = "性别[ 0.女  1.男  2.未知]")
    private Byte gender;

    /**
     * 所属部门
     */
    @Schema(description = "所属部门")
    private Long deptId;

    /**
     * 过期时间  为空就是永久
     */
    @Schema(description = "过期时间,为空就是永久")
    private LocalDateTime expired;

    /**
     * 登录时间
     */
    @Schema(description = "登录时间")
    private LocalDateTime loginTime;

    /**
     * 角色列表
     */
    @Schema(description = "角色列表")
    private Collection<PmsRoleDTO> roles;

    /**
     * 拥有的租户ID
     */
    @Schema(description = "拥有的租户ID")
    private Set<String> haveTenantIds;

    /**
     * 权限列表
     */
    @Schema(description = "权限列表")
    private Collection<StringGrantedAuthority> authorities;

    @JsonIgnore
    @Override
    public String getPassword() {
        return this.password;
    }


    @Override
    public String getUsername() {
        return this.username;
    }


    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        if (expired == null) {
            return true;
        }
        return LocalDateTime.now().isBefore(expired);
    }


    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return status == StatusEnum.NORMAL;
    }


    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public String getName() {
        return this.loginId;
    }


}
