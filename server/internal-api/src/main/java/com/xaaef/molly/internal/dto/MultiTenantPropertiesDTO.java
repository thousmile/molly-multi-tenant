package com.xaaef.molly.internal.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 多租户全局配置
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/9 11:53
 */

@Getter
@Setter
public class MultiTenantPropertiesDTO {

    /**
     * 是否开启租户模式
     */
    private Boolean enable;

    /**
     * 是否开启租户模式
     */
    private Boolean enableProject;

    /**
     * 数据库名称前缀
     */
    private String prefix;

    /**
     * 默认租户ID
     */
    private String defaultTenantId;

    /**
     * 默认 项目ID
     */
    private Long defaultProjectId;

    /**
     * 默认 数据库名称
     */
    private String dbName;

}
