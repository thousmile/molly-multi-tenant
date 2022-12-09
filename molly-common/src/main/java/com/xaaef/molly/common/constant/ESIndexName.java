package com.xaaef.molly.common.constant;


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
     * 索引前缀 ， 隔离 开发环境 和 测试环境使用 同一个 ES 集群的问题
     */
    public static final String INDEX_PREFIX = "dev_";

    /**
     * 操作日志
     */
    public static final String OPERATE_LOG_INDEX = INDEX_PREFIX + "operate_logs";


    /**
     * 登录日志
     */
    public static final String LOGIN_LOG_INDEX = INDEX_PREFIX + "login_logs";


}
