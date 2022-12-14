package com.xaaef.molly.system.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xaaef.molly.core.tenant.base.service.impl.BaseServiceImpl;
import com.xaaef.molly.system.entity.CommDictData;
import com.xaaef.molly.common.enums.DefaultFlag;
import com.xaaef.molly.system.mapper.CommDictDataMapper;
import com.xaaef.molly.system.po.DictQueryPO;
import com.xaaef.molly.system.service.CommDictDataService;
import com.xaaef.molly.system.vo.DictDataVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
public class CommDictDataServiceImpl extends BaseServiceImpl<CommDictDataMapper, CommDictData>
        implements CommDictDataService {


    @Override
    public IPage<CommDictData> pageKeywords(DictQueryPO params) {
        Page<CommDictData> page = Page.of(params.getPageIndex(), params.getPageSize());
        QueryWrapper<CommDictData> wrapper = getKeywordsQueryWrapper(params, null);
        wrapper.lambda().eq(CommDictData::getTypeKey, params.getDictTypeKey());
        return super.page(page, wrapper);
    }


    @Override
    public List<CommDictData> listByKey(String dictTypeKey) {
        return super.listByEq(CommDictData::getTypeKey, dictTypeKey);
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
        collect.values().forEach(v -> {
                    v.sort((o1, o2) -> {
                        long temp = o1.getDictSort() - o2.getDictSort();
                        if (temp > 0)
                            return 1;
                        else if (temp < 0)
                            return -1;
                        else
                            return 0;
                    });
                }
        );
        return collect;
    }

}
