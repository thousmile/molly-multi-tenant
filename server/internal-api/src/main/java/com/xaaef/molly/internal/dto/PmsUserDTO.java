package com.xaaef.molly.internal.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 系统用户
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
public class PmsUserDTO implements java.io.Serializable {

    /**
     * 用户唯一id
     */
    private Long userId;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 密码
     */
    private String password;

    /**
     * 性别[ 0.女  1.男  2.未知]
     */
    private Byte gender;

    /**
     * 是否管理员 0. 普通用户  1. 管理员
     */
    private Byte adminFlag;

    /**
     * 状态 【0.禁用 1.正常】
     */
    private Byte status;

    /**
     * 所属部门
     */
    private Long deptId;

    /**
     * 过期时间  为空就是永久
     */
    private LocalDateTime expired;

}
