package com.xaaef.molly.tenant.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.internal.api.ApiOperateUserService;
import com.xaaef.molly.tenant.base.BaseEntity;
import com.xaaef.molly.tenant.base.service.BaseService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.xaaef.molly.common.consts.MbpConst.CREATE_TIME;


/**
 * <p>
 * 基础
 * </p>
 *
 * @author Wang Chen Chen<932560435@qq.com>
 * @date 2021/10/21 21:39
 */

@Slf4j
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {

    @Lazy
    @Resource
    private ApiOperateUserService apiOperateUserService;


    @Override
    public void reflectionFill(Object objList) {
        apiOperateUserService.reflectionFill(objList);
    }


    @Override
    public boolean save(T entity) {
        saveFill(entity);
        return baseMapper.insert(entity) > 0;
    }


    @Override
    public boolean saveBatch(Collection<T> entityList) {
        entityList.forEach(this::saveFill);
        return super.saveBatch(entityList);
    }


    @Override
    public boolean updateById(T entity) {
        updateFill(entity);
        return super.updateById(entity);
    }


    @Override
    public boolean update(T entity, Wrapper<T> updateWrapper) {
        updateFill(entity);
        return super.update(entity, updateWrapper);
    }


    @Override
    public boolean updateBatchById(Collection<T> entityList) {
        entityList.forEach(this::updateFill);
        return super.updateBatchById(entityList);
    }


    @Override
    public boolean exist(SFunction<T, ?> column, Object value) {
        var wrapper = new LambdaQueryWrapper<T>().eq(column, value);
        return super.count(wrapper) > 0;
    }


    @Override
    public boolean exist(Wrapper<T> wrapper) {
        return super.count(wrapper) > 0;
    }


    @Override
    public long count(SFunction<T, ?> column, Object value) {
        var wrapper = new LambdaQueryWrapper<T>().eq(column, value);
        return super.count(wrapper);
    }


    @Override
    public T getOne(SFunction<T, ?> column, Object value) {
        return super.getOne(new LambdaQueryWrapper<T>().eq(column, value));
    }


    protected QueryWrapper<T> getKeywordsQueryWrapper(SearchPO params, Collection<SFunction<T, ?>> columns) {
        var wrapper = new QueryWrapper<T>();
        // 开始时间是否为空
        if (ObjectUtils.isNotEmpty(params.getStartDate())) {
            // 如果结束时间是否为空
            if (ObjectUtils.isNotEmpty(params.getEndDate())) {
                wrapper.between(CREATE_TIME, params.getStartDate(), params.getEndDate());
            } else {
                wrapper.between(CREATE_TIME, params.getStartDate(), LocalDate.now());
            }
        }
        if (StringUtils.isNotBlank(params.getKeywords()) && columns != null && !columns.isEmpty()) {
            var index = 0;
            for (SFunction<T, ?> column : columns) {
                if (index == 0)
                    wrapper.lambda().like(column, params.getKeywords());
                else
                    wrapper.lambda().or().like(column, params.getKeywords());
                index++;
            }
        }
        return wrapper;
    }


    @Override
    public IPage<T> pageKeywords(SearchPO params, Collection<SFunction<T, ?>> columns) {
        Page<T> pageRequest = Page.of(params.getPageIndex(), params.getPageSize());
        QueryWrapper<T> wrapper = getKeywordsQueryWrapper(params, columns);
        Page<T> result = super.page(pageRequest, wrapper);
        if (params.getIncludeCauu() != null && params.getIncludeCauu()) {
            reflectionFill(result.getRecords());
        }
        return result;
    }


    @Override
    public List<T> listByEq(SFunction<T, ?> column, Object value) {
        return super.list(new LambdaQueryWrapper<T>().eq(column, value));
    }


    @Override
    public List<T> listByEq(Map<SFunction<T, ?>, Object> columns) {
        var wrapper = new LambdaQueryWrapper<T>();
        columns.forEach(wrapper::eq);
        return super.list(wrapper);
    }


    @Override
    public List<T> listByIn(SFunction<T, ?> column, Collection<?> values) {
        var wrapper = new LambdaQueryWrapper<T>()
                .in(column, values);
        return super.list(wrapper);
    }


}
