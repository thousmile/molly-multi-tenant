package com.xaaef.molly.system.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;

/**
 * <p>
 * 菜单权限
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 2.0
 * @date 2019/4/18 11:45
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuVO implements java.io.Serializable {

    /**
     * 菜单 ID
     */
    @JsonIgnore
    private Long id;

    /**
     * 上级菜单
     */
    @JsonIgnore
    private Long parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 路径
     */
    private String path;

    /**
     * 组件
     */
    private String component;

    /**
     * 排序
     */
    @JsonIgnore
    private Integer sort;

    /**
     *
     */
    private MenuMeta meta;


    /**
     * 菜单状态
     */
    private boolean hidden;

    /**
     * 子菜单
     */
    private List<MenuVO> children;


    @Getter
    @Setter
    @ToString
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MenuMeta {

        /**
         * 菜单图标
         */
        private String icon;

        /**
         * 菜单图标
         */
        private String title;

    }

}
