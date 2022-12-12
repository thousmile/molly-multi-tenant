package com.xaaef.molly.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xaaef.molly.common.domain.TreeNode;
import com.xaaef.molly.core.tenant.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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

@Entity
@Table(name = "comm_menu")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysMenu extends BaseEntity implements TreeNode<SysMenu> {

    /**
     * 菜单 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    /**
     * 上级菜单
     */
    @Column(nullable = false)
    private Long parentId;

    /**
     * 菜单 名称
     */
    @Column(nullable = false)
    private String menuName;

    /**
     * 权限标识
     */
    @Column(nullable = false)
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
    @Column(nullable = false)
    private Integer sort;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 0.租户用户   1. 系统用户   2.全部
     */
    @Column(nullable = false)
    private Byte target;

    /**
     * 菜单类型（0.菜单 1.按钮）
     */
    @Column(nullable = false)
    private Byte menuType;

    /**
     * 菜单状态（1.显示 0.隐藏）
     */
    @Column(nullable = false)
    private Byte visible;

    /**
     * 子节点
     */
    @Transient
    private List<SysMenu> children;


    @JsonIgnore
    @Override
    public Long getId() {
        return this.menuId;
    }

    @Override
    public List<SysMenu> getChildren() {
        return this.children;
    }

    @Override
    public void setChildren(List<SysMenu> list) {
        this.children = list;
    }

}
