package com.xaaef.molly.perms.entity;

import com.xaaef.molly.core.tenant.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


/**
 * <p>
 * 部门表
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */


@Entity
@Table(name = "sys_dept")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysDept extends BaseEntity {

    /**
     * 部门 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deptId;

    /**
     * 部门上级
     */
    @Column(nullable = false)
    private Long parentId;

    /**
     * 部门 名称
     */
    @Column(nullable = false)
    private String deptName;

    /**
     * 部门 领导名称
     */
    @Column(nullable = false)
    private String leader;

    /**
     * 部门 领导手机号
     */
    @Column(nullable = false)
    private String leaderMobile;

    /**
     * 排序
     */
    @Column(nullable = false)
    private Long sort;

    /**
     * 部门描述
     */
    private String description;

    /**
     * 祖级列表
     */
    @Column(nullable = false)
    private String ancestors;

}
