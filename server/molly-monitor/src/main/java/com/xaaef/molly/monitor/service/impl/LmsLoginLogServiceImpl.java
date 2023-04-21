package com.xaaef.molly.monitor.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.monitor.entity.LmsLoginLog;
import com.xaaef.molly.monitor.repository.LmsLoginLogRepository;
import com.xaaef.molly.monitor.service.LmsLoginLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;


/**
 * <p>
 * 登录日志
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/2/6 15:28
 */

@Slf4j
@Service
@AllArgsConstructor
public class LmsLoginLogServiceImpl implements LmsLoginLogService {

    private final LmsLoginLogRepository loginLogRepository;


    @Override
    public IPage<LmsLoginLog> pageKeywords(SearchPO params) {
        return null;
    }


    @Override
    public boolean removeBatchByIds(Set<String> ids) {
        loginLogRepository.deleteAllById(ids);
        return true;
    }


}
