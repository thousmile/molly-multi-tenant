package com.xaaef.molly.internal.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xaaef.molly.common.consts.RegexConst;
import com.xaaef.molly.common.valid.ValidCreate;
import com.xaaef.molly.common.valid.ValidUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Schema(description = "部门ID")
    @NotNull(message = "部门ID,必须填写", groups = {ValidUpdate.class})
    private Long deptId;

    /**
     * 部门上级
     */
    @Schema(description = "上级部门ID")
    @NotNull(message = "上级部门ID,必须填写", groups = {ValidCreate.class})
    private Long parentId;

    /**
     * 部门 名称
     */
    @Schema(description = "部门名称")
    @NotBlank(message = "部门名称,必须填写", groups = {ValidCreate.class})
    private String deptName;

    /**
     * 部门 领导名称
     */
    @Schema(description = "领导名称")
    @NotBlank(message = "领导名称,必须填写", groups = {ValidCreate.class})
    private String leader;

    /**
     * 部门 领导手机号
     */
    @Schema(description = "领导手机号")
    @NotBlank(message = "领导手机号,必须填写", groups = {ValidCreate.class})
    @Pattern(regexp = RegexConst.MOBILE, message = "领导手机号,格式错误", groups = {ValidCreate.class, ValidUpdate.class})
    private String leaderMobile;

    /**
     * 排序
     */
    @Schema(description = "排序")
    @NotNull(message = "部门排序,必须填写", groups = {ValidCreate.class})
    private Long sort;

    /**
     * 部门描述
     */
    @Schema(description = "部门描述")
    @NotBlank(message = "部门描述,必须填写", groups = {ValidCreate.class})
    private String description;

    /**
     * 祖级列表
     */
    @Schema(description = "祖级列表")
    @JsonIgnore
    private String ancestors;

}
