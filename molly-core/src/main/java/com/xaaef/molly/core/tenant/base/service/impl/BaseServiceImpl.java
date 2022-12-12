package com.xaaef.molly.core.tenant.base.service.impl;

import com.xaaef.molly.core.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.core.tenant.base.BaseEntity;
import com.xaaef.molly.core.tenant.base.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * <p>
 * </p>
 *
 * @author Wang Chen Chen<932560435@qq.com>
 * @date 2021/10/21 21:39
 */

@Slf4j
public class BaseServiceImpl<R extends JpaRepository<T, ID>, T extends BaseEntity, ID> implements BaseService<T, ID> {

    @Autowired
    protected R baseReps;


    /**
     * 插入时 填写
     */
    protected void saveFill(T entity) {
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
    protected void updateFill(T entity) {
        var userId = JwtSecurityUtils.getLoginUser().getUserId();
        entity.setCreateTime(null);
        entity.setCreateUser(null);
        if (userId != null) {
            entity.setLastUpdateUser(userId);
        }
        entity.setLastUpdateTime(LocalDateTime.now());
    }


    @Override
    public List<T> findAll() {
        return baseReps.findAll();
    }


    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        return baseReps.findAllById(ids);
    }


    @Override
    public <S extends T> List<S> findAll(Example<S> example) {
        return baseReps.findAll(example);
    }


    @Override
    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        return baseReps.findAll(example, sort);
    }


    @Override
    public Page<T> findPage(Pageable pageable) {
        return baseReps.findAll(pageable);
    }

    @Override
    public <S extends T> Page<S> findPage(Example<S> example, Pageable pageable) {
        return baseReps.findAll(example, pageable);
    }


    @Override
    public Optional<T> findById(ID id) {
        return baseReps.findById(id);
    }


    @Override
    public T getById(ID id) {
        return findById(id).get();
    }


    @Override
    public boolean existsById(ID id) {
        return baseReps.existsById(id);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public T save(T entity) {
        saveFill(entity);
        return baseReps.save(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Collection<T> saveBatch(Collection<T> entity) {
        entity.forEach(this::saveFill);
        return baseReps.saveAll(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public T updateById(T entity) {
        updateFill(entity);
        return baseReps.saveAndFlush(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Collection<T> updateByIdBatch(Collection<T> arr) {
        arr.forEach(this::updateFill);
        return baseReps.saveAllAndFlush(arr);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(ID id) {
        baseReps.deleteById(id);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAllById(Collection<ID> ids) {
        baseReps.deleteAllById(ids);
    }


}
