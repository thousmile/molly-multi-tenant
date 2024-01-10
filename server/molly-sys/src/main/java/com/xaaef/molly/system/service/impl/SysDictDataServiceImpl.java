package com.xaaef.molly.system.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xaaef.molly.common.enums.DefaultFlag;
import com.xaaef.molly.system.entity.SysDictData;
import com.xaaef.molly.system.mapper.SysDictDataMapper;
import com.xaaef.molly.system.po.DictQueryPO;
import com.xaaef.molly.system.service.SysDictDataService;
import com.xaaef.molly.system.vo.DictDataVO;
import com.xaaef.molly.tenant.base.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
public class SysDictDataServiceImpl extends BaseServiceImpl<SysDictDataMapper, SysDictData>
        implements SysDictDataService {


    @Override
    public IPage<SysDictData> pageKeywords(DictQueryPO params) {
        Page<SysDictData> page = Page.of(params.getPageIndex(), params.getPageSize());
        QueryWrapper<SysDictData> wrapper = getKeywordsQueryWrapper(params, null);
        wrapper.lambda().eq(SysDictData::getTypeKey, params.getDictTypeKey());
        return super.page(page, wrapper);
    }


    @Override
    public List<SysDictData> listByKey(String dictTypeKey) {
        return super.listByEq(SysDictData::getTypeKey, dictTypeKey);
    }


    @Override
    public Map<String, List<DictDataVO>> mapByKeys() {
        var collect = super.list().stream()
                .map(source -> {
                    DictDataVO target = new DictDataVO();
                    BeanUtils.copyProperties(source, target);
                    if (StringUtils.isNumeric(source.getDictValue())) {
                        if (NumberUtil.isInteger(source.getDictValue())) {
                            target.setDictValue(NumberUtil.parseInt(source.getDictValue()));
                        } else if (NumberUtil.isLong(source.getDictValue())) {
                            target.setDictValue(NumberUtil.parseLong(source.getDictValue()));
                        } else if (NumberUtil.isDouble(source.getDictValue())) {
                            target.setDictValue(NumberUtil.parseDouble(source.getDictValue()));
                        }
                    } else {
                        target.setDictValue(source.getDictValue());
                    }
                    target.setIsDefault(DefaultFlag.get(source.getIsDefault()) == DefaultFlag.YES);
                    return target;
                })
                .collect(Collectors.groupingBy(DictDataVO::getTypeKey));
        collect.values()
                .forEach(v ->
                        v.sort(Comparator.comparingLong(DictDataVO::getDictSort))
                );
        return collect;
    }

}
