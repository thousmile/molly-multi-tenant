package com.xaaef.molly.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaaef.molly.core.tenant.base.service.impl.BaseServiceImpl;
import com.xaaef.molly.system.entity.CommDictData;
import com.xaaef.molly.system.entity.CommDictType;
import com.xaaef.molly.system.mapper.CommDictTypeMapper;
import com.xaaef.molly.system.service.CommDictDataService;
import com.xaaef.molly.system.service.CommDictTypeService;
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
public class CommDictTypeServiceImpl extends BaseServiceImpl<CommDictTypeMapper, CommDictType>
        implements CommDictTypeService {

    private final CommDictDataService dictDataService;


    @Override
    public boolean save(CommDictType entity) {
        if (exist(CommDictType::getTypeKey, entity.getTypeKey())) {
            throw new RuntimeException(
                    String.format("字典关键字 %s 已经存在了", entity.getTypeKey())
            );
        }
        return super.save(entity);
    }


    @Override
    public boolean updateById(CommDictType entity) {
        var wrapper = new LambdaQueryWrapper<CommDictType>()
                .eq(CommDictType::getTypeKey, entity.getTypeKey())
                .notIn(CommDictType::getTypeId, entity.getTypeId());
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
        CommDictType dictType = getById(id);
        if (dictType == null) {
            throw new RuntimeException(
                    String.format("字典类型Id %s 不存在", id)
            );
        }
        dictDataService.remove(new LambdaQueryWrapper<CommDictData>()
                .eq(CommDictData::getTypeKey, dictType.getTypeKey()));
        return super.removeById(id);
    }


}
