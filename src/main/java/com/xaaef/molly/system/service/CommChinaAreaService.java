package com.xaaef.molly.system.service;

import com.xaaef.molly.system.entity.CommChinaArea;

import java.util.Collection;
import java.util.Optional;



public interface CommChinaAreaService {


    Optional<CommChinaArea> findById(Long id);


    CommChinaArea save(CommChinaArea entity);


    Collection<CommChinaArea> saveBatch(Collection<CommChinaArea> entity);


    CommChinaArea updateById(CommChinaArea entity);


    Collection<CommChinaArea> updateByIdBatch(Collection<CommChinaArea> arr);


    void deleteById(Long id);


    void deleteAllById(Collection<Long> ids);


}
