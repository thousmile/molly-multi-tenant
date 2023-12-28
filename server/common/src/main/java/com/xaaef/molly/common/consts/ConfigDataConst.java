package com.xaaef.molly.common.consts;

import org.apache.commons.collections4.KeyValue;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

/**
 * <p>
 * 全局配置文件 key
 * DefConfigValueConst
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/16 15:15
 */

public class ConfigDataConst {

    /**
     * 系统配置
     */
    public static final String REDIS_CACHE_KEY = "sys_config_cache";


    /**
     * 用户默认密码
     */
    public static final KeyValue<String, String> DEFAULT_USER_PASSWORD = new DefaultKeyValue<>("default_user_password", "1234546");


    /**
     * 项目默认密码
     */
    public static final KeyValue<String, String> DEFAULT_PROJECT_PASSWORD = new DefaultKeyValue<>("default_project_password", "1234546");


    /**
     * 每个租户的 默认项目ID
     */
    public static final KeyValue<String, Long> DEFAULT_PROJECT_ID = new DefaultKeyValue<>("default_project_id", 10001L);


    /**
     * 租户默认logo
     */
    public static final KeyValue<String, String> DEFAULT_TENANT_LOGO = new DefaultKeyValue<>("default_tenant_logo", "https://images.xaaef.com/molly_master_logo.png");


    /**
     * 租户默认 角色名称
     */
    public static final KeyValue<String, String> DEFAULT_ROLE_NAME = new DefaultKeyValue<>("default_role_name", "操作员");


    /**
     * 每个租户的 默认角色 ID
     */
    public static final KeyValue<String, Long> DEFAULT_ROLE_ID = new DefaultKeyValue<>("default_role_id", 10001L);


    /**
     * 每个租户的 默认部门ID
     */
    public static final KeyValue<String, Long> DEFAULT_DEPT_ID = new DefaultKeyValue<>("default_dept_id", 10001L);

    /**
     * 获取法定节假日的接口
     */
    public static final KeyValue<String, String> GET_HOLIDAY_URL = new DefaultKeyValue<>("get_holiday_url", "https://timor.tech/api/holiday/year/%s?type=N&week=Y");


}
