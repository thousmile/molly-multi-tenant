package com.xaaef.molly.system.vo;

import com.xaaef.molly.common.domain.TreeNode;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *     角色，部门，租户模板 等... 关联菜单
 * </p>
 *
 * @author Wang Chen Chen<932560435@qq.com>
 * @version 1.0.1
 * @date 2021/7/24 14:39
 */

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMenusVO implements java.io.Serializable {

    /**
     * 已经拥有的菜单 ID
     */
    Set<Long> have;

    /**
     * 全部 菜单 ID 和 名称
     */
    List<MenuIdName> all;

    @Getter
    @Setter
    @ToString
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MenuIdName implements TreeNode<MenuIdName> {

        private Long id;

        private Long parentId;

        private Integer sort;

        private String title;

        private List<MenuIdName> children;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            MenuIdName that = (MenuIdName) o;

            return new EqualsBuilder().append(id, that.id).append(parentId, that.parentId).append(sort, that.sort).append(title, that.title).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37).append(id).append(parentId).append(sort).append(title).toHashCode();
        }

    }

}


