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
    public static <S> S delegate(String targetTenantId, Supplier<S> fun) {
        // 获取 原始 租户ID
        var originalTenantId = TenantUtils.getTenantId();
        // 使用 自定义的 租户ID
        TenantUtils.setUseCustomTenantId(true);
        // 设置 目标 租户ID
        TenantUtils.setTenantId(targetTenantId);
        // 调用目标方法
        var result = fun.get();
        // 设置 原始 租户ID
        TenantUtils.setTenantId(originalTenantId);
        // 清除 使用 自定义的 租户ID
        TenantUtils.setUseCustomTenantId(null);
        return result;
    }


}
