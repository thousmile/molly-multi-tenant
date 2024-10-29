package com.xaaef.molly.corems.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaaef.molly.common.consts.RegexConst;
import com.xaaef.molly.common.valid.ValidCreate;
import com.xaaef.molly.common.valid.ValidDelete;
import com.xaaef.molly.common.valid.ValidUpdate;
import com.xaaef.molly.internal.dto.PmsDeptDTO;
import com.xaaef.molly.tenant.base.ParamBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.Accessors;


/**
 * <p>
 * 项目表
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2023/10/10 9:31
 */

@Schema(description = "项目")
@TableName("cms_project")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CmsProject extends ParamBaseEntity {

    /**
     * 项目ID
     */
    @Schema(description = "项目ID")
    @TableId(type = IdType.AUTO)
    @NotNull(message = "项目ID,必须填写", groups = {ValidUpdate.class, ValidDelete.class})
    private Long projectId;

    /**
     * 项目名称
     */
    @Schema(description = "项目名称")
    @NotEmpty(message = "项目名称,必须填写", groups = {ValidCreate.class})
    private String projectName;

    /**
     * 联系人名称
     */
    @Schema(description = "联系人名称")
    @NotEmpty(message = "联系人名称,必须填写", groups = {ValidCreate.class})
    private String linkman;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    @NotEmpty(message = "联系电话,必须填写", groups = {ValidCreate.class})
    @Pattern(regexp = RegexConst.TEL_PHONE, message = "联系电话,格式错误", groups = {ValidCreate.class, ValidUpdate.class})
    private String contactNumber;

    /**
     * 行政地址
     */
    @Schema(description = "行政地址")
    @NotNull(message = "行政地址,必须填写", groups = {ValidCreate.class})
    private Long areaCode;

    /**
     * 联系地址 如：左右云创谷1栋A座
     */
    @Schema(description = "联系地址")
    @NotEmpty(message = "联系地址,必须填写", groups = {ValidCreate.class})
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
    @NotEmpty(message = "项目密码,必须填写", groups = {ValidDelete.class})
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
    @NotNull(message = "所属部门Id,必须填写", groups = {ValidCreate.class})
    private Long deptId;

    /**
     * 所属部门
     */
    @Schema(description = "所属部门")
    @TableField(exist = false)
    private PmsDeptDTO dept;

}
