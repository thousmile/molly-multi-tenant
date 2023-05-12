package com.xaaef.molly.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaaef.molly.tenant.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

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
    @TableId
    private String tenantId;

    /**
     * 租户 logo
     */
    @Schema(description = "租户logo")
    private String logo;

    /**
     * 租户名称
     */
    @Schema(description = "租户名称")
    private String name;

    /**
     * 租户邮箱
     */
    @Schema(description = "租户邮箱")
    private String email;

    /**
     * 联系人名称
     */
    @Schema(description = "联系人名称")
    private String linkman;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    private String contactNumber;

    /**
     * 行政地址
     */
    @Schema(description = "行政地址")
    private Long areaCode;

    /**
     * 联系地址
     */
    @Schema(description = "联系地址")
    private String address;

    /**
     * 状态 【0.禁用 1.正常】
     */
    @Schema(description = "状态 【0.禁用 1.正常】")
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
    private Set<SysTemplate> templates;


}
