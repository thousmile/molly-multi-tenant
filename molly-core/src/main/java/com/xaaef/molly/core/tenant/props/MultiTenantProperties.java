package com.xaaef.molly.core.tenant.props;

import com.xaaef.molly.core.tenant.enums.DbStyle;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
@ConfigurationProperties(prefix = "multi.tenant")
public class MultiTenantProperties {


    /**
     * 数据库名称前缀
     */
    private String prefix = "molly_";


    /**
     * 默认租户ID
     */
    private String defaultTenantId = "master";


    /**
     * 多租户的类型。默认是 schema
     */
    private DbStyle dbStyle = DbStyle.SCHEMA;


    /**
     * 创建表结构
     */
    private Boolean createTable = Boolean.TRUE;


}
