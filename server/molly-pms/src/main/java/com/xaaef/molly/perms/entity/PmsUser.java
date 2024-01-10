package com.xaaef.molly.perms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaaef.molly.common.consts.RegexConst;
import com.xaaef.molly.tenant.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

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


@Schema(description = "用户")
@TableName("pms_user")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PmsUser extends BaseEntity {

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @TableId(type = IdType.AUTO)
    @NotNull(message = "用户ID,必须填写", groups = {ValidUpdate.class})
    private Long userId;

    /**
     * 头像
     */
    @Schema(description = "头像")
    @NotBlank(message = "头像,必须填写", groups = {ValidCreate.class})
    @URL(message = "头像,格式错误", groups = {ValidCreate.class, ValidUpdate.class})
    private String avatar;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @NotBlank(message = "用户名,必须填写", groups = {ValidCreate.class})
    @Length(min = 5, max = 32, message = "用户名,长度5~32位字符", groups = {ValidCreate.class, ValidUpdate.class})
    private String username;

    /**
     * 手机号码
     */
    @Schema(description = "手机号码")
    @NotBlank(message = "手机号码,必须填写", groups = {ValidCreate.class})
    @Pattern(regexp = RegexConst.MOBILE, message = "手机号码,格式错误", groups = {ValidCreate.class, ValidUpdate.class})
    private String mobile;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    @NotBlank(message = "邮箱,必须填写", groups = {ValidCreate.class})
    @Email(message = "邮箱,格式错误", groups = {ValidCreate.class, ValidUpdate.class})
    private String email;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    @NotBlank(message = "昵称,必须填写", groups = {ValidCreate.class})
    private String nickname;

    /**
     * 密码
     */
    @Schema(description = "密码")
    @Length(min = 5, max = 32, message = "密码,长度5~32位字符", groups = {ValidCreate.class, ValidUpdate.class})
    private String password;

    /**
     * 性别[ 0.女  1.男  2.未知]
     */
    @Schema(description = "性别[ 0.女  1.男  2.未知]")
    @NotNull(message = "性别,必须填写", groups = {ValidCreate.class})
    private Byte gender;

    /**
     * 是否管理员 0. 普通用户  1. 管理员
     */
    @Schema(description = "是否管理员 0. 普通用户  1. 管理员")
    private Byte adminFlag;

    /**
     * 状态 【 0.禁用  1.正常】
     */
    @Schema(description = "状态 【 0.禁用  1.正常】")
    private Byte status;

    /**
     * 所属部门
     */
    @Schema(description = "所属部门Id")
    @NotNull(message = "所属部门Id,必须填写", groups = {ValidCreate.class})
    private Long deptId;

    /**
     * 过期时间  为空就是永久
     */
    @Schema(description = "过期时间 为空就是永久")
    private LocalDateTime expired;

    /**
     * 部门信息
     */
    @Schema(description = "部门信息")
    @TableField(exist = false)
    private PmsDept dept;

    /**
     * 角色名称，列表
     */
    @Schema(description = "角色列表")
    @TableField(exist = false)
    @NotNull(message = "角色列表,必须填写", groups = {ValidCreate.class})
    @Size(min = 1, message = "角色列表,最少选择1个", groups = {ValidCreate.class, ValidUpdate.class})
    private Set<PmsRole> roles;

    /**
     * 如果为空，表示 未登录。
     */
    @Schema(description = "如果为空，表示 未登录")
    @TableField(exist = false)
    private String loginId;

    /**
     * 用户 创建分组
     */
    public interface ValidCreate {
    }

    /**
     * 用户 修改分组
     */
    public interface ValidUpdate {
    }

}
