package com.xaaef.molly.tenant.service;


import com.xaaef.molly.common.domain.SmallTenant;

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
     * TODO 判断当前租户是否默认租户ID
     *
     * @author WangChenChen
     * @date 2022/12/11 9:46
     */
    boolean isDefaultTenantId(String tenantId);


    /**
     * TODO 根据租户ID 判断当前租户是否存在
     *
     * @author WangChenChen
     * @date 2022/12/11 9:46
     */
    boolean existById(String tenantId);


    /**
     * TODO 根据租户ID 获取信息
     *
     * @author WangChenChen
     * @date 2022/12/11 9:46
     */
    SmallTenant getById(String tenantId);


    /**
     * TODO 添加租户ID
     *
     * @author WangChenChen
     * @date 2022/12/11 9:46
     */
    void addTenantId(SmallTenant tenant);


    /**
     * TODO 添加租户ID
     *
     * @author WangChenChen
     * @date 2022/12/11 9:46
     */
    void addTenantIdBatch(Set<SmallTenant> tenants);


    /**
     * TODO 添加租户ID
     *
     * @author WangChenChen
     * @date 2022/12/11 9:46
     */
    void removeTenantId(String tenantId);


    /**
     * TODO 删除 所有租户 ID
     *
     * @author WangChenChen
     * @date 2022/12/11 9:46
     */
    void removeAll();


}
