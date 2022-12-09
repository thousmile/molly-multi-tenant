package com.xaaef.molly.perms.entity;

import com.xaaef.molly.core.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * <p>
 * 系统用户
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */


@Entity
@Table(name = "sys_user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUser extends BaseEntity {

    /**
     * 用户唯一id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
