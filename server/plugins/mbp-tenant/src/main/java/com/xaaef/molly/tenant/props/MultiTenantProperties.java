package com.xaaef.molly.tenant.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
@Component
@ConfigurationProperties(prefix = "multi.tenant")
public class MultiTenantProperties {

    /**
     * 是否开启租户模式
     */
    private Boolean enable = true;

    /**
     * 是否开启项目模式
     */
    private Boolean enableProject = true;

    /**
     * 数据库名称前缀
     */
    private String prefix = "molly_";

    /**
     * 默认租户ID
     */
    private String defaultTenantId = "master";

    /**
     * 默认 项目ID
     */
    private Long defaultProjectId = 10001L;

    /**
     * 默认 数据库名称
     */
    private String dbName = prefix + defaultTenantId;

    /**
     * 创建表结构
     */
    private Boolean createTable = true;

    /**
     * Liquibase contexts
     */
    private String contexts = "tenant";

    /**
     * Liquibase 创建表结构的 Liquibase 文件地址
     */
    private String changeLog = "classpath:db/changelog-master.xml";

}
