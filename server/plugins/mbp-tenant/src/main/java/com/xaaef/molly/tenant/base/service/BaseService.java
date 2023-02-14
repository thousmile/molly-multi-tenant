package com.xaaef.molly.tenant.base.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.tenant.base.BaseEntity;
import com.xaaef.molly.tenant.util.TenantUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


/**
 * <p>
 * 通用 service 实现类
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 2.0
 * @date 2019/4/18 11:45
 */


public interface BaseService<T extends BaseEntity> extends IService<T> {

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
    default <S> S delegate(String targetTenant, Supplier<S> fun) {
        var originalTenantId = TenantUtils.getTenantId();
        TenantUtils.setTenantId(targetTenant);
        var result = fun.get();
        TenantUtils.setTenantId(originalTenantId);
        return result;
    }


    /**
     * 插入时 填写
     */
    default <S extends BaseEntity> void saveFill(S entity) {
        var userId = JwtSecurityUtils.getLoginUser().getUserId();
        if (null == entity.getCreateUser() && userId != null) {
            entity.setCreateUser(userId);
        }
        if (null == entity.getLastUpdateUser() && userId != null) {
            entity.setLastUpdateUser(userId);
        }
        entity.setCreateTime(LocalDateTime.now());
        entity.setLastUpdateTime(LocalDateTime.now());
    }


    /**
     * 修改时 填写
     */
    default <S extends BaseEntity> void updateFill(S entity) {
        var userId = JwtSecurityUtils.getLoginUser().getUserId();
        entity.setCreateTime(null);
        entity.setCreateUser(null);
        if (userId != null) {
            entity.setLastUpdateUser(userId);
        }
        entity.setLastUpdateTime(LocalDateTime.now());
    }


    /**
     * 根据关键字查询
     *
     * @param params
     * @param columns 实体类字段
     * @author Wang Chen Chen
     * @date 2021/8/25 9:41
     */
    IPage<T> pageKeywords(SearchPO params, SFunction<T, ?>... columns);


    /**
     * 根据关键字查询
     *
     * @param params
     * @param columns 实体类字段
     * @author Wang Chen Chen
     * @date 2021/8/25 9:41
     */
    IPage<T> pageKeywords(SearchPO params, List<SFunction<T, ?>> columns);


    /**
     * 根据 条件 查询全部
     *
     * @author Wang Chen Chen
     * @date 2021/8/25 9:41
     */
    List<T> listByEq(SFunction<T, ?> column, Object value);


    /**
     * 根据 多条件 查询全部
     *
     * @param columns
     * @author Wang Chen Chen
     * @date 2021/8/25 9:41
     */
    List<T> listByEq(Map<SFunction<T, ?>, Object> columns);


    /**
     * 根据 多条件 查询全部
     *
     * @param values
     * @author Wang Chen Chen
     * @date 2021/8/25 9:41
     */
    List<T> listByIn(SFunction<T, ?> column, Collection<?> values);


    /**
     * 根据 字段 判断，是否存在
     *
     * @author Wang Chen Chen
     * @date 2021/8/25 9:41
     */
    boolean exist(SFunction<T, ?> column, Object value);


    /**
     * 根据 字段 判断，数量
     *
     * @return
     * @author Wang Chen Chen
     * @date 2021/8/25 9:41
     */
    long count(SFunction<T, ?> column, Object value);


    /**
     * 根据 字段 判断，是否存在
     *
     * @author Wang Chen Chen
     * @date 2021/8/25 9:41
     */
    T getOne(SFunction<T, ?> column, Object value);


}
