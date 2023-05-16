package com.xaaef.molly.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaaef.molly.tenant.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;


/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */

@Schema(description = "权限菜单")
@TableName("sys_menu")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysMenu extends BaseEntity {

    /**
     * 菜单 ID
     */
    @Schema(description = "菜单ID")
    @TableId(type = IdType.AUTO)
    private Long menuId;

    /**
     * 上级菜单
     */
    @Schema(description = "上级菜单")
    private Long parentId;

    /**
     * 菜单 名称
     */
    @Schema(description = "菜单名称")
    private String menuName;

    /**
     * 权限标识
     */
    @Schema(description = "权限标识")
    private String perms;

    /**
     * 组件
     */
    @Schema(description = "组件")
    private String component;

    /**
     * 菜单图标
     */
    @Schema(description = "菜单图标")
    private String icon;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Long sort;

    /**
     * 路由地址
     */
    @Schema(description = "路由地址")
    private String path;

    /**
     * 0.租户用户   1. 系统用户   2.全部
     */
    @Schema(description = "目标用户: 0.租户用户 1. 系统用户 2.全部")
    private Byte target;

    /**
     * 菜单类型（0.菜单 1.按钮）
     */
    @Schema(description = "菜单类型（0.菜单 1.按钮）")
    private Byte menuType;

    /**
     * 菜单状态（1.显示 0.隐藏）
     */
    @Schema(description = "菜单状态（1.显示 0.隐藏）")
    private Byte visible;

    /**
     * 保持状态（1.保持 0.不保持）
     */
    @Schema(description = "保持状态（1.保持 0.不保持）")
    private Byte keepAlive;

}
