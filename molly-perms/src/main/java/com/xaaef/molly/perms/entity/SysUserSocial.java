package com.xaaef.molly.perms.entity;

import com.xaaef.molly.core.tenant.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


/**
 * <p>
 * 用户社交账号
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */


@Entity
@Table(name = "sys_user_social")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUserSocial extends BaseEntity {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long socialId;

    /**
     * 头像
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * 社交账号唯一ID
     */
    @Column(nullable = false)
    private String openId;

    /**
     * we_chat. 微信  tencent_qq. 腾讯QQ
     */
    @Column(nullable = false)
    private String socialType;

}
