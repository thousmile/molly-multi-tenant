package com.xaaef.molly.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaaef.molly.tenant.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "数据 ID")
    @TableId(type = IdType.AUTO)
    private Long dictCode;

    /**
     * 字典排序
     */
    @Schema(description = "排序")
    private Long dictSort;

    /**
     * 字典标签
     */
    @Schema(description = "标签")
    private String dictLabel;

    /**
     * 字典键值
     */
    @Schema(description = "键值")
    private String dictValue;

    /**
     * 字典类型关键字
     */
    @Schema(description = "类型关键字")
    private String typeKey;

    /**
     * 是否默认（1.是 0.否）
     */
    @Schema(description = "是否默认（1.是 0.否）")
    private Byte isDefault;

}
