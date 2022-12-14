package com.xaaef.molly.perms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaaef.molly.core.tenant.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */

@TableName("pms_role")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PmsRole extends BaseEntity {

    /**
     * 部门 ID
     */
    @TableId(type = IdType.AUTO)
    private Long roleId;

    /**
     * 部门 名称
     */
    private String roleName;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 部门描述
     */
    private String description;

}
