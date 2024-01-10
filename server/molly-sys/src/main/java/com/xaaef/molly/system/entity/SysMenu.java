package com.xaaef.molly.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaaef.molly.tenant.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "菜单ID,必须填写", groups = {ValidUpdate.class})
    private Long menuId;

    /**
     * 上级菜单
     */
    @Schema(description = "上级菜单")
    @NotNull(message = "上级菜单,必须填写", groups = {ValidCreate.class})
    private Long parentId;

    /**
     * 菜单 名称
     */
    @Schema(description = "菜单名称")
    @NotEmpty(message = "菜单名称,必须填写", groups = {ValidCreate.class})
    private String menuName;

    /**
     * 权限标识
     */
    @Schema(description = "权限标识")
    @NotEmpty(message = "权限标识,必须填写", groups = {ValidCreate.class})
    private String perms;

    /**
     * 组件
     */
    @Schema(description = "组件")
    @NotEmpty(message = "组件,必须填写", groups = {ValidCreate.class})
    private String component;

    /**
     * 菜单图标
     */
    @Schema(description = "菜单图标")
    @NotEmpty(message = "菜单图标,必须填写", groups = {ValidCreate.class})
    private String icon;

    /**
     * 排序
     */
    @Schema(description = "排序")
    @NotNull(message = "排序,必须填写", groups = {ValidCreate.class})
    private Long sort;

    /**
     * 路由地址
     */
    @Schema(description = "路由地址")
    @NotEmpty(message = "路由地址,必须填写", groups = {ValidCreate.class})
    private String path;

    /**
     * 0.租户用户   1. 系统用户   2.全部
     */
    @Schema(description = "目标用户: 0.租户用户 1. 系统用户 2.全部")
    @NotNull(message = "目标用户,必须填写", groups = {ValidCreate.class})
    private Byte target;

    /**
     * 菜单类型（0.菜单 1.按钮 2.外链）
     */
    @Schema(description = "菜单类型（0.菜单 1.按钮 2.外链）")
    @NotNull(message = "菜单类型,必须填写", groups = {ValidCreate.class})
    private Byte menuType;

    /**
     * 菜单状态（1.显示 0.隐藏）
     */
    @Schema(description = "菜单状态（1.显示 0.隐藏）")
    @NotNull(message = "菜单状态,必须填写", groups = {ValidCreate.class})
    private Byte visible;

    /**
     * 保持状态（1.保持 0.不保持）
     */
    @Schema(description = "保持状态（1.保持 0.不保持）")
    @NotNull(message = "保持状态,必须填写", groups = {ValidCreate.class})
    private Byte keepAlive;

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
