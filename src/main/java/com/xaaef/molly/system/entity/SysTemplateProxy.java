package com.xaaef.molly.system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * <p>
 * 租户的 权限模板表
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysTemplateProxy extends SysTemplate {

    /**
     * 租户 ID
     */
    private String tenantId;


}
