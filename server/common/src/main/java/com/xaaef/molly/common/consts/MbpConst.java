package com.xaaef.molly.common.consts;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * mybatis-plus 常量配置
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/16 15:15
 */

public class MbpConst {


    /**
     * 数据字段。
     */
    public static final String PROJECT_ID = "project_id";

    public final static String CREATE_TIME = "create_time";

    public final static String CREATE_USER = "create_user";

    public final static String LAST_UPDATE_TIME = "last_update_time";

    public final static String LAST_UPDATE_USER = "last_update_user";


    /**
     * 属性字段。
     */
    public final static String ATTR_PROJECT_ID = "projectId";

    public final static String ATTR_CREATE_TIME = "createTime";

    public final static String ATTR_CREATE_USER = "createUser";

    public final static String ATTR_LAST_UPDATE_TIME = "lastUpdateTime";

    public final static String ATTR_LAST_UPDATE_USER = "lastUpdateUser";

    /**
     * 基础属性字段
     */
    public final static Set<String> ATTR_BASE_FIELDS = Set.of(
            ATTR_CREATE_TIME,
            ATTR_CREATE_USER,
            ATTR_LAST_UPDATE_TIME,
            ATTR_LAST_UPDATE_USER
    );

    /**
     * 不需要进行 拦截租户 的表名称
     * 用法:
     *
     * @see com.xaaef.molly.tenant.schema.SchemaInterceptor.ignoreTable()
     */
    public final static Set<String> TENANT_IGNORE_TABLES = new HashSet<>(
            Set.of(
                    "sys_tenant",                   // 租户
                    "sys_tenant_template",          // 租户关联模板
                    "sys_template",                 // 模板
                    "sys_template_menu",            // 模板关联菜单
                    "sys_menu",                     // 菜单
                    "sys_config",                   // 全局配置
                    "sys_dict_data",                // 数据字典数据
                    "sys_dict_type",                // 数据字典类型
                    "sys_file_detail",              // 通用配置文件
                    "china_area"                    // 中国行政区域表
            )
    );


    /**
     * 不需要进行拦截 project_id 的表名称
     *
     * <p>
     * 在 项目初始化的时候，会自动从数据中读取，不包含 project_id 字段的表名称
     * <p>
     * 必须添加 COLUMNS 否则，初始化的时候，自动读取表名称
     * <p>
     *
     * @see com.xaaef.molly.corems.runner.ProjectTableRunner
     * <p>
     */
    public final static Set<String> PROJECT_IGNORE_TABLES = new HashSet<>(
            Stream.of(TENANT_IGNORE_TABLES, Set.of("COLUMNS"))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet())
    );


}
