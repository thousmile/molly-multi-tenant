package com.xaaef.molly.tenant.consts;

import java.util.List;

/**
 * <p>
 * 全局配置文件 key
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/16 15:15
 */

public class MbpConst {

    // http 租户请求头
    public final static String X_TENANT_ID = "x-tenant-id";

    // http 租户 默认项目 请求头
    public final static String X_PROJECT_ID = "x-project-id";


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
     * 不需要进行 拦截租户 的表名称
     */
    public final static List<String> TENANT_IGNORE_TABLES = List.of(
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
    );


    /**
     * 不需要进行 拦截项目ID 的表名称
     */
    public final static List<String> PROJECT_IGNORE_TABLES = List.of();


}
