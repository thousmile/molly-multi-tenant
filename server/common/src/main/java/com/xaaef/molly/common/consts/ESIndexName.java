package com.xaaef.molly.common.consts;


import com.xaaef.molly.common.util.TenantUtils;

/**
 * <p>
 * es 索引名称
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/8/10 18:25
 */

public class ESIndexName {

    /**
     * 操作日志 索引名称
     */
    private static final String OPERATE_LOG_INDEX = "_lms_oper_log";

    /**
     * 获取 操作日志 索引名称
     */
    public static String getOperateLogIndex() {
        return TenantUtils.getTenantId() + OPERATE_LOG_INDEX;
    }


    /**
     * 登录日志
     */
    private static final String LOGIN_LOG_INDEX = "_lms_login_log";


    /**
     * 获取 操作日志 索引名称
     */
    public static String getLoginLogIndex() {
        return TenantUtils.getTenantId() + LOGIN_LOG_INDEX;
    }


    /**
     * 设备日志
     */
    private static final String DEVICE_LOG_INDEX = "_dms_device_log";


    /**
     * 获取 设备日志 索引名称
     */
    public static String getDeviceLogIndex() {
        return TenantUtils.getTenantId() + DEVICE_LOG_INDEX;
    }

}
