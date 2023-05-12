package com.xaaef.molly.perms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaaef.molly.tenant.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

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
public class PmsRole extends BaseEntity {

    /**
     * 角色 ID
     */
    @Schema(description = "角色ID")
    @TableId(type = IdType.AUTO)
    private Long roleId;

    /**
     * 角色 名称
     */
    @Schema(description = "角色名称")
    private String roleName;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Long sort;

    /**
     * 角色描述
     */
    @Schema(description = "角色描述")
    private String description;

}
