package com.xaaef.molly.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaaef.molly.tenant.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 字典数据表
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/15 10:42
 */

@Schema(description = "字典数据")
@TableName("sys_dict_data")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysDictData extends BaseEntity {

    /**
     * 字典数据 ID
     */
    @Schema(description = "数据ID")
    @TableId(type = IdType.AUTO)
    @NotNull(message = "字典数据ID,必须填写", groups = {ValidUpdate.class})
    private Long dictCode;

    /**
     * 字典排序
     */
    @Schema(description = "排序")
    @NotNull(message = "排序,必须填写", groups = {ValidCreate.class})
    private Long dictSort;

    /**
     * 字典标签
     */
    @Schema(description = "标签")
    @NotEmpty(message = "标签,必须填写", groups = {ValidCreate.class})
    private String dictLabel;

    /**
     * 字典键值
     */
    @Schema(description = "键值")
    @NotEmpty(message = "键值,必须填写", groups = {ValidCreate.class})
    private String dictValue;

    /**
     * 字典类型关键字
     */
    @Schema(description = "类型关键字")
    @NotEmpty(message = "类型关键字,必须填写", groups = {ValidCreate.class})
    private String typeKey;

    /**
     * 是否默认（1.是 0.否）
     */
    @Schema(description = "是否默认（1.是 0.否）")
    @NotNull(message = "类型关键字,必须填写", groups = {ValidCreate.class})
    private Byte isDefault;

    /**
     * 配置 保存分组
     */
    public interface ValidCreate {
    }

    /**
     * 配置 修改分组
     */
    public interface ValidUpdate {
    }

}
