package com.xaaef.molly.core.tenant.service;


import java.util.Set;

/**
 * TODO 多租户管理器
 * 项目初始化的时候，将数据库的全部租户ID,
 * 添加到 redis 中。每次请求，都验证 租户ID 是否存在，
 *
 * @author WangChenChen
 * @date 2022/12/11 9:45
 */

public interface MultiTenantManager {

    /**
     * TODO 获取默认租户
     *
     * @author WangChenChen
     * @date 2022/12/11 9:46
     */
    String getDefaultTenantId();


    /**
     * TODO 根据租户ID 判断当前租户是否存在
     *
     * @author WangChenChen
     * @date 2022/12/11 9:46
     */
    boolean existById(String tenantId);


    /**
     * TODO 添加租户ID
     *
     * @author WangChenChen
     * @date 2022/12/11 9:46
     */
    void addTenantId(String tenantId);


    /**
     * TODO 添加租户ID
     *
     * @author WangChenChen
     * @date 2022/12/11 9:46
     */
    void addTenantIdBatch(Set<String> tenantIds);


    /**
     * TODO 添加租户ID
     *
     * @author WangChenChen
     * @date 2022/12/11 9:46
     */
    void removeTenantId(String tenantId);


}
