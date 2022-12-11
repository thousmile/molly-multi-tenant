package com.xaaef.molly.system.service;

import com.xaaef.molly.system.entity.ChinaArea;

import java.util.Collection;
import java.util.Optional;

public interface ChinaAreaService {


    Optional<ChinaArea> findById(Long id);


    ChinaArea save(ChinaArea entity);


    Collection<ChinaArea> saveBatch(Collection<ChinaArea> entity);


    ChinaArea updateById(ChinaArea entity);


    Collection<ChinaArea> updateByIdBatch(Collection<ChinaArea> arr);


    void deleteById(Long id);


    void deleteAllById(Collection<Long> ids);


}
