package com.xaaef.molly.internal.dto;

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

@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysMenuDTO implements java.io.Serializable {

    /**
     * 菜单 ID
     */
    private Long menuId;

    /**
     * 上级菜单
     */
    private Long parentId;

    /**
     * 菜单 名称
     */
    private String menuName;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 组件
     */
    private String component;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 0.租户用户   1. 系统用户   2.全部
     */
    private Byte target;

    /**
     * 菜单类型（0.菜单 1.按钮）
     */
    private Byte menuType;

    /**
     * 菜单状态（1.显示 0.隐藏）
     */
    private Byte visible;

}
