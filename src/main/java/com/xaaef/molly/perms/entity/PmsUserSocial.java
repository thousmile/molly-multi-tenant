package com.xaaef.molly.perms.entity;

import com.xaaef.molly.core.tenant.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;


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
@Table(name = "pms_user_social")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PmsUserSocial extends BaseEntity {

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
