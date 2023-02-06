package com.xaaef.molly.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.monitor.entity.LmsLoginLog;
import com.xaaef.molly.monitor.entity.LmsOperLog;
import com.xaaef.molly.monitor.mapper.LmsOperLogMapper;
import com.xaaef.molly.monitor.service.LmsOperLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * <p>
 * 操作日志
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/2/6 15:28
 */

@Slf4j
@Service
@AllArgsConstructor
public class LmsOperLogServiceImpl extends ServiceImpl<LmsOperLogMapper, LmsOperLog> implements LmsOperLogService {


    @Override
    public IPage<LmsOperLog> pageKeywords(SearchPO params) {
        Page<LmsOperLog> pageRequest = Page.of(params.getPageIndex(), params.getPageSize());
        var wrapper = new LambdaQueryWrapper<LmsOperLog>();
        // 开始时间是否为空
        if (ObjectUtils.isNotEmpty(params.getStartDate())) {
            // 如果结束时间是否为空
            if (ObjectUtils.isNotEmpty(params.getEndDate())) {
                wrapper.between(LmsOperLog::getCreateTime, params.getStartDate(), params.getEndDate());
            } else {
                wrapper.between(LmsOperLog::getCreateTime, params.getStartDate(), LocalDate.now());
            }
        }
        if (StringUtils.isNotBlank(params.getKeywords())) {
            wrapper.like(LmsOperLog::getCreateTime, params.getKeywords());
        }
        return super.page(pageRequest, wrapper);
    }


}
