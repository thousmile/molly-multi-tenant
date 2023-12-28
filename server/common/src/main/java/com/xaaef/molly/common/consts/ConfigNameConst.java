package com.xaaef.molly.common.consts;

import org.apache.commons.collections4.KeyValue;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

/**
 * <p>
 * 全局配置文件 key
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/16 15:15
 */

public class ConfigNameConst {

    /**
     * 系统配置
     */
    public static final String REDIS_CACHE_KEY = "sys_config_cache";


    /**
     * 用户默认密码
     */
    public static final KeyValue<String, String> USER_DEFAULT_PASSWORD = new DefaultKeyValue<>("default_user_password", "1234546");


    /**
     * 项目默认密码
     */
    public static final KeyValue<String, String> PROJECT_DEFAULT_PASSWORD = new DefaultKeyValue<>("default_project_password", "1234546");


    /**
     * 租户默认logo
     */
    public static final KeyValue<String, String> TENANT_DEFAULT_LOGO = new DefaultKeyValue<>("default_tenant_logo", "https://images.xaaef.com/molly_master_logo.png");


    /**
     * 租户默认角色名称
     */
    public static final KeyValue<String, String> TENANT_DEFAULT_ROLE_NAME = new DefaultKeyValue<>("default_role_name", "操作员");


    /**
     * 获取法定节假日的接口
     */
    public static final KeyValue<String, String> GET_HOLIDAY_URL = new DefaultKeyValue<>("get_holiday_url", "https://timor.tech/api/holiday/year/%s?type=N&week=Y");


}
