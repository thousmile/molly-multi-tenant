package com.xaaef.molly.perms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaaef.molly.internal.dto.PmsDeptDTO;
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

@Schema(description = "项目")
@TableName("cms_project")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CmsProject extends BaseEntity {

    /**
     * 项目ID
     */
    @Schema(description = "项目ID")
    @TableId(type = IdType.AUTO)
    private Long projectId;

    /**
     * 项目名称
     */
    @Schema(description = "项目名称")
    private String projectName;

    /**
     * 联系人名称
     */
    @Schema(description = "联系人名称")
    private String linkman;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    private String contactNumber;

    /**
     * 行政地址
     */
    @Schema(description = "行政地址")
    private Long areaCode;

    /**
     * 联系地址 如：左右云创谷1栋A座
     */
    @Schema(description = "联系地址")
    private String address;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Long sort;

    /**
     * 项目密码，做一些危险操作时，使用
     */
    @Schema(description = "项目密码，做一些危险操作时，使用")
    private String password;

    /**
     * 状态 【0.禁用 1.正常 2.锁定 】
     */
    @Schema(description = "状态 【0.禁用 1.正常 2.锁定 】")
    private Byte status;

    /**
     * 所属部门
     */
    @Schema(description = "所属部门Id")
    private Long deptId;

    /**
     * 所属部门
     */
    @Schema(description = "所属部门")
    @TableField(exist = false)
    private PmsDeptDTO dept;

}
