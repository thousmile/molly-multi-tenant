package com.xaaef.molly.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaaef.molly.tenant.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;


/**
 * <p>
 * 租户的 权限模板表
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */

@Schema(description = "租户权限模板")
@TableName("sys_template")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysTemplate extends BaseEntity {

    /**
     * 模板 ID
     */
    @Schema(description = "模板 ID")
    @TableId(type = IdType.AUTO)
    @NotNull(message = "模板ID,必须填写", groups = {ValidUpdate.class})
    private Long id;

    /**
     * 模板名称
     */
    @Schema(description = "模板名称")
    @NotEmpty(message = "模板名称,必须填写", groups = {ValidCreate.class})
    private String name;

    /**
     * 模板描述
     */
    @Schema(description = "模板描述")
    @NotEmpty(message = "模板描述,必须填写", groups = {ValidCreate.class})
    private String description;

    /**
     * 菜单 Id
     */
    @TableField(exist = false)
    @Schema(description = "菜单Id")
    private Set<Long> menuIds;

    /**
     * 配置 保存分组
     */
    public interface ValidCreate {
    }

    /**
     * 配置 修改分组
     */
    public interface ValidUpdate {
    }

}
