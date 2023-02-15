package com.xaaef.molly.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaaef.molly.tenant.base.BaseEntity;
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
    @TableId(type = IdType.AUTO)
    private Long typeId;

    /**
     * 字典类型名
     */
    private String typeName;

    /**
     * 字典类型关键字
     */
    private String typeKey;

    /**
     * 字典描述
     */
    private String description;

}
