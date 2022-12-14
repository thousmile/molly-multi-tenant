package com.xaaef.molly.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xaaef.molly.common.domain.TreeNode;
import com.xaaef.molly.core.tenant.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;


/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */

@TableName("comm_menu")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CommMenu extends BaseEntity implements TreeNode<CommMenu> {

    /**
     * 菜单 ID
     */
    @TableId(type = IdType.AUTO)
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
    private Integer sort;

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

    /**
     * 子节点
     */
    @TableField(exist = false)
    private List<CommMenu> children;


    @JsonIgnore
    @Override
    public Long getId() {
        return this.menuId;
    }

    @Override
    public List<CommMenu> getChildren() {
        return this.children;
    }

    @Override
    public void setChildren(List<CommMenu> list) {
        this.children = list;
    }

}
