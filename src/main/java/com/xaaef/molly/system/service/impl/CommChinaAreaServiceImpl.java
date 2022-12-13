package com.xaaef.molly.system.service.impl;

import com.xaaef.molly.system.entity.CommChinaArea;
import com.xaaef.molly.system.repository.CommChinaAreaRepository;
import com.xaaef.molly.system.service.CommChinaAreaService;
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
public class CommChinaAreaServiceImpl implements CommChinaAreaService {

    private final CommChinaAreaRepository baseReps;

    @Override
    public Optional<CommChinaArea> findById(Long id) {
        return baseReps.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommChinaArea save(CommChinaArea entity) {
        return baseReps.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Collection<CommChinaArea> saveBatch(Collection<CommChinaArea> entity) {
        return baseReps.saveAll(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommChinaArea updateById(CommChinaArea entity) {
        return baseReps.saveAndFlush(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Collection<CommChinaArea> updateByIdBatch(Collection<CommChinaArea> arr) {
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
