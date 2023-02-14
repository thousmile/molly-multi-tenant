package com.xaaef.molly.perms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaaef.molly.tenant.base.BaseEntity;
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


@TableName("pms_user_social")
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
    @TableId(type = IdType.AUTO)
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
