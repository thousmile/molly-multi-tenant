package com.xaaef.molly.perms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaaef.molly.common.valid.ValidCreate;
import com.xaaef.molly.common.valid.ValidUpdate;
import com.xaaef.molly.tenant.base.ParamBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */

@Schema(description = "角色")
@TableName("pms_role")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PmsRole extends ParamBaseEntity {

    /**
     * 角色 ID
     */
    @Schema(description = "角色ID")
    @TableId(type = IdType.AUTO)
    @NotNull(message = "角色ID,必须填写", groups = {ValidUpdate.class})
    private Long roleId;

    /**
     * 角色 名称
     */
    @Schema(description = "角色名称")
    @NotBlank(message = "角色名称,必须填写", groups = {ValidCreate.class})
    private String roleName;

    /**
     * 排序
     */
    @Schema(description = "排序")
    @NotNull(message = "排序,必须填写", groups = {ValidCreate.class})
    private Long sort;

    /**
     * 角色描述
     */
    @Schema(description = "角色描述")
    @NotBlank(message = "角色描述,必须填写", groups = {ValidCreate.class})
    private String description;

    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：仅本部门数据权限 4：本部门及以下数据权限 5：仅自己的权限）
     */
    @Schema(description = "数据范围", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "数据范围,必须填写", groups = {ValidCreate.class})
    private Integer dataScope;

    /**
     * 自定义数据权限
     */
    @Schema(description = "自定义数据权限")
    @TableField(exist = false)
    private Set<Long> deptIds;

}
