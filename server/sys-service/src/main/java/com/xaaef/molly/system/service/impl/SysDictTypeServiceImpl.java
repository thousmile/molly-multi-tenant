package com.xaaef.molly.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaaef.molly.tenant.base.service.impl.BaseServiceImpl;
import com.xaaef.molly.system.entity.SysDictData;
import com.xaaef.molly.system.entity.SysDictType;
import com.xaaef.molly.system.mapper.SysDictTypeMapper;
import com.xaaef.molly.system.service.SysDictDataService;
import com.xaaef.molly.system.service.SysDictTypeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/9 15:36
 */


@Slf4j
@Service
@AllArgsConstructor
public class SysDictTypeServiceImpl extends BaseServiceImpl<SysDictTypeMapper, SysDictType>
        implements SysDictTypeService {

    private final SysDictDataService dictDataService;


    @Override
    public boolean save(SysDictType entity) {
        if (exist(SysDictType::getTypeKey, entity.getTypeKey())) {
            throw new RuntimeException(
                    String.format("字典关键字 %s 已经存在了", entity.getTypeKey())
            );
        }
        return super.save(entity);
    }


    @Override
    public boolean updateById(SysDictType entity) {
        var wrapper = new LambdaQueryWrapper<SysDictType>()
                .eq(SysDictType::getTypeKey, entity.getTypeKey())
                .notIn(SysDictType::getTypeId, entity.getTypeId());
        if (super.count(wrapper) > 0) {
            throw new RuntimeException(
                    String.format("字典关键字 %s 已经存在了", entity.getTypeKey())
            );
        }
        return super.updateById(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeById(Serializable id) {
        SysDictType dictType = getById(id);
        if (dictType == null) {
            throw new RuntimeException(
                    String.format("字典类型Id %s 不存在", id)
            );
        }
        dictDataService.remove(new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getTypeKey, dictType.getTypeKey()));
        return super.removeById(id);
    }


}
