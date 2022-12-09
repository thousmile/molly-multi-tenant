package com.xaaef.molly.system.service.impl;

import com.xaaef.molly.system.entity.ChinaArea;
import com.xaaef.molly.system.repository.ChinaAreaRepository;
import com.xaaef.molly.system.service.ChinaAreaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/9 15:29
 */

@Slf4j
@Service
@AllArgsConstructor
public class ChinaAreaServiceImpl implements ChinaAreaService {

    private final ChinaAreaRepository baseReps;

    @Override
    public Optional<ChinaArea> findById(Long id) {
        return baseReps.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ChinaArea save(ChinaArea entity) {
        return baseReps.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Collection<ChinaArea> saveBatch(Collection<ChinaArea> entity) {
        return baseReps.saveAll(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ChinaArea updateById(ChinaArea entity) {
        return baseReps.saveAndFlush(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Collection<ChinaArea> updateByIdBatch(Collection<ChinaArea> arr) {
        return baseReps.saveAllAndFlush(arr);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long id) {
        baseReps.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAllById(Collection<Long> ids) {
        baseReps.deleteAllById(ids);
    }

}
