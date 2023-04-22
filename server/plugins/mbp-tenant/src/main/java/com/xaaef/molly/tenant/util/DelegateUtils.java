package com.xaaef.molly.tenant.util;

import com.xaaef.molly.common.util.TenantUtils;

import java.util.function.Supplier;


public class DelegateUtils {


    /**
     * 委托其他租户执行，此方法
     * <p>
     * 如: 新增租户的时候，客户端传入的 租户id是 : master
     * 新增完成租户后，需要初始化一些，用户数据。就需要插入 新的租户ID 所属的库中了。
     * 执行完成后，再将租户ID设置为，原始的 master
     *
     * @author WangChenChen
     * @date 2022/12/14 16:33
     */
    public static <S> S delegate(String targetTenant, Supplier<S> fun) {
        var originalTenantId = TenantUtils.getTenantId();
        TenantUtils.setTenantId(targetTenant);
        var result = fun.get();
        TenantUtils.setTenantId(originalTenantId);
        return result;
    }


}
