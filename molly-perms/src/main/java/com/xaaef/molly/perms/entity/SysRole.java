package com.xaaef.molly.perms.entity;

import com.xaaef.molly.core.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */

@Entity
@Table(name = "sys_role")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysRole extends BaseEntity {

    /**
     * 部门 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    /**
     * 部门 名称
     */
    private String roleName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 部门描述
     */
    private String description;

}
