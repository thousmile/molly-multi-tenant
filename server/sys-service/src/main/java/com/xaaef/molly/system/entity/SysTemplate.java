package com.xaaef.molly.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaaef.molly.tenant.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;


/**
 * <p>
 * 租户的 权限模板表
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */

@TableName("sys_template")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysTemplate extends BaseEntity {

    /**
     * 模板 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 租户 logo
     */
    private String name;

    /**
     * 租户名称
     */
    private String description;

    /**
     * 菜单 Id
     */
    @TableField(exist = false)
    private Set<Long> menuIds;

}
