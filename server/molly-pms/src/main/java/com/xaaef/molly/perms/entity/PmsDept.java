package com.xaaef.molly.perms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaaef.molly.tenant.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;



/**
 * <p>
 * 部门表
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */

@Schema(description = "部门")
@TableName("pms_dept")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PmsDept extends BaseEntity {

    /**
     * 部门 ID
     */
    @Schema(description = "部门ID")
    @TableId(type = IdType.AUTO)
    private Long deptId;

    /**
     * 部门上级
     */
    @Schema(description = "上级部门")
    private Long parentId;

    /**
     * 部门 名称
     */
    @Schema(description = "部门名称")
    private String deptName;

    /**
     * 部门 领导名称
     */
    @Schema(description = "领导名称")
    private String leader;

    /**
     * 部门 领导手机号
     */
    @Schema(description = "领导手机号")
    private String leaderMobile;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Long sort;

    /**
     * 部门描述
     */
    @Schema(description = "部门描述")
    private String description;

    /**
     * 祖级列表
     */
    @Schema(description = "祖级列表")
    private String ancestors;

}
