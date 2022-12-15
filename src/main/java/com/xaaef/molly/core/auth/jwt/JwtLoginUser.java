package com.xaaef.molly.core.auth.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xaaef.molly.core.auth.enums.AdminFlag;
import com.xaaef.molly.core.auth.enums.GrantType;
import com.xaaef.molly.core.auth.enums.StatusEnum;
import com.xaaef.molly.core.auth.enums.UserType;
import com.xaaef.molly.perms.entity.PmsRoleProxy;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * <p>
 * spring security 认证 UserDetails 实现类
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 2.0
 * @date 2019/4/18 11:45
 */

@ToString
@Getter
@Setter
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtLoginUser implements UserDetails {

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 登录ID
     */
    private String loginId;

    /**
     * 认证授权方式
     */
    private GrantType grantType;

    /**
     * 用户唯一ID
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户登录时，使用的用户名
     */
    private String username;

    /**
     * 用户登录时，使用的密码
     */
    private String password;

    /**
     * 用户状态， [ 0.禁用 1.正常 2.被删除 ]
     */
    private StatusEnum status;

    /**
     * 是否管理员 0. 普通用户  1. 管理员
     */
    private AdminFlag adminFlag;

    /**
     * 用户类型 0. 租户用户  1. 系统用户
     */
    private UserType userType;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 角色列表
     */
    private Collection<PmsRoleProxy> roles;

    /**
     * 权限列表
     */
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
        return true;
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


}
