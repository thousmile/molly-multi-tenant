package com.xaaef.molly.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.system.entity.ChinaArea;
import com.xaaef.molly.system.mapper.ChinaAreaMapper;
import com.xaaef.molly.system.service.ChinaAreaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

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
public class ChinaAreaServiceImpl extends ServiceImpl<ChinaAreaMapper, ChinaArea>
        implements ChinaAreaService {


    @Override
    public IPage<ChinaArea> pageKeywords(SearchPO params) {
        var wrapper = new LambdaQueryWrapper<ChinaArea>();
        Page<ChinaArea> page = Page.of(params.getPageIndex(), params.getPageSize());
        if (StringUtils.isNotBlank(params.getKeywords())) {
            wrapper.like(ChinaArea::getName, params.getKeywords())
                    .or()
                    .like(ChinaArea::getMergerName, params.getKeywords())
                    .or()
                    .like(ChinaArea::getPinyin, params.getKeywords());
        }
        return baseMapper.selectPage(page, wrapper);
    }

    
}
