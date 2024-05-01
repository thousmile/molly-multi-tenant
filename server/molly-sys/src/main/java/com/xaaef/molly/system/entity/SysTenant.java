package com.xaaef.molly.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaaef.molly.common.consts.RegexConst;
import com.xaaef.molly.common.valid.ValidCreate;
import com.xaaef.molly.common.valid.ValidUpdate;
import com.xaaef.molly.tenant.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.Set;


/**
 * <p>
 * 租户
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */


@Schema(description = "租户")
@TableName("sys_tenant")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysTenant extends BaseEntity {

    /**
     * 租户 ID
     */
    @Schema(description = "租户ID")
    @TableId(type = IdType.INPUT)
    @NotEmpty(message = "租户ID,必须填写", groups = {ValidUpdate.class})
    private String tenantId;

    /**
     * 租户 logo
     */
    @Schema(description = "租户logo")
    @NotEmpty(message = "租户logo,必须填写", groups = {ValidCreate.class})
    @URL(message = "租户logo,格式错误", groups = {ValidCreate.class, ValidUpdate.class})
    private String logo;

    /**
     * 租户名称
     */
    @Schema(description = "租户名称")
    @NotEmpty(message = "租户名称,必须填写", groups = {ValidCreate.class})
    private String name;

    /**
     * 租户邮箱
     */
    @Schema(description = "租户邮箱")
    @NotEmpty(message = "租户邮箱,必须填写", groups = {ValidCreate.class})
    private String email;

    /**
     * 联系人名称
     */
    @Schema(description = "联系人名称")
    @NotEmpty(message = "联系人名称,必须填写", groups = {ValidCreate.class})
    private String linkman;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    @NotEmpty(message = "联系电话,必须填写", groups = {ValidCreate.class})
    @Pattern(regexp = RegexConst.TEL_PHONE, message = "联系电话,格式错误", groups = {ValidCreate.class, ValidUpdate.class})
    private String contactNumber;

    /**
     * 行政地址
     */
    @Schema(description = "行政地址")
    @NotNull(message = "行政地址,必须填写", groups = {ValidCreate.class})
    private Long areaCode;

    /**
     * 联系地址
     */
    @Schema(description = "联系地址")
    @NotEmpty(message = "联系地址,必须填写", groups = {ValidCreate.class})
    private String address;

    /**
     * 状态 【 0.禁用  1.正常  9.初始化中】
     */
    @Schema(description = "状态 【0.禁用 1.正常 9.初始化中】")
    private Byte status;

    /**
     * 过期时间  默认是 100 年
     */
    @Schema(description = "过期时间 默认是 10 年")
    private LocalDateTime expired;

    /**
     * 租户 模板
     */
    @Schema(description = "租户模板")
    @TableField(exist = false)
    @NotNull(message = "租户模板,必须填写", groups = {ValidCreate.class})
    @Size(min = 1, message = "租户模板,最少选择1个", groups = {ValidCreate.class, ValidUpdate.class})
    private Set<SysTemplate> templates;

}
