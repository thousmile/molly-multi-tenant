package com.xaaef.molly.tenant;

import com.xaaef.molly.tenant.props.MultiTenantProperties;

import javax.sql.DataSource;
import java.util.Set;
import java.util.function.Consumer;

public interface DatabaseManager {

    /**
     * TODO 获取默认的数据源
     *
     * @author WangChenChen
     * @date 2022/12/11 11:04
     */

    DataSource getDefaultDataSource();


    /**
     * TODO 根据租户ID 获取对应的数据源
     *
     * @author WangChenChen
     * @date 2022/12/11 11:04
     */

    DataSource getDataSource(String tenantId);


    /**
     * TODO 租户创建表结构
     *
     * @author WangChenChen
     * @date 2022/12/11 11:04
     */
    void asyncUpdateTable(String tenantId, Consumer<Exception> consumer);


    /**
     * TODO 租户创建表结构
     *
     * @author WangChenChen
     * @date 2022/12/11 11:04
     */
    void updateTable(String tenantId) throws Exception;


    /**
     * TODO 租户创建表结构
     *
     * @author WangChenChen
     * @date 2022/12/11 11:04
     */
    void updateTable(Set<String> tenantIds) throws Exception;


    /**
     * TODO 租户删除表结构
     *
     * @author WangChenChen
     * @date 2022/12/11 11:04
     */
    void deleteTable(String tenantId);


    /**
     * TODO 获取多租户信息
     *
     * @author WangChenChen
     * @date 2022/12/11 11:04
     */
    MultiTenantProperties getMultiTenantProperties();


}
