package com.xaaef.molly.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaaef.molly.common.valid.ValidCreate;
import com.xaaef.molly.common.valid.ValidUpdate;
import com.xaaef.molly.tenant.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 字典类型表
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/15 10:42
 */

@Schema(description = "字典类型")
@TableName("sys_dict_type")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysDictType extends BaseEntity {

    /**
     * 字典类型 ID
     */
    @Schema(description = "字典类型 ID")
    @TableId(type = IdType.AUTO)
    @NotNull(message = "字典类型ID,必须填写", groups = {ValidUpdate.class})
    private Long typeId;

    /**
     * 字典类型名
     */
    @Schema(description = "类型名")
    @NotEmpty(message = "类型名,必须填写", groups = {ValidCreate.class})
    private String typeName;

    /**
     * 字典类型关键字
     */
    @Schema(description = "关键字")
    @NotEmpty(message = "关键字,必须填写", groups = {ValidCreate.class})
    private String typeKey;

    /**
     * 字典描述
     */
    @Schema(description = "描述")
    @NotEmpty(message = "描述,必须填写", groups = {ValidCreate.class})
    private String description;

}
