package com.xaaef.molly.perms.entity;

import com.xaaef.molly.core.base.BaseEntity;
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
    private Long userId;

    /**
     * 社交账号唯一ID
     */
    private String openId;

    /**
     * we_chat. 微信  tencent_qq. 腾讯QQ
     */
    private String socialType;

}
