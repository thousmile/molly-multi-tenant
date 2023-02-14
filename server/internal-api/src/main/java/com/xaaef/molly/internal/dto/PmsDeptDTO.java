package com.xaaef.molly.internal.dto;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author WangChenChen
 * @version 1.2
 * @date 2022/6/24 16:33
 */


@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PmsDeptDTO implements java.io.Serializable {

    /**
     * 部门 ID
     */
    private Long deptId;

    /**
     * 部门上级
     */
    private Long parentId;

    /**
     * 部门 名称
     */
    private String deptName;

    /**
     * 部门 领导名称
     */
    private String leader;

    /**
     * 部门 领导手机号
     */
    private String leaderMobile;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 部门描述
     */
    private String description;

    /**
     * 祖级列表
     */
    private String ancestors;

}
