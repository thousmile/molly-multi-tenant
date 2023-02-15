package com.xaaef.molly.monitor.api.impl;

import com.xaaef.molly.internal.api.ApiLogStorageService;
import com.xaaef.molly.internal.dto.LoginLogDTO;
import com.xaaef.molly.internal.dto.OperLogDTO;
import com.xaaef.molly.monitor.entity.LmsLoginLog;
import com.xaaef.molly.monitor.entity.LmsOperLog;
import com.xaaef.molly.monitor.mapper.LmsLoginLogMapper;
import com.xaaef.molly.monitor.mapper.LmsOperLogMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/2/14 11:48
 */


@Slf4j
@Service
@AllArgsConstructor
public class ApiLogStorageServiceImpl implements ApiLogStorageService {


    private final LmsLoginLogMapper loginLogMapper;


    private final LmsOperLogMapper operLogMapper;


    @Override
    public void asyncLoginSave(LoginLogDTO source) {
        var target = new LmsLoginLog();
        BeanUtils.copyProperties(source, target);
        loginLogMapper.insert(target);
    }


    @Override
    public void asyncOperateSave(OperLogDTO source) {
        var target = new LmsOperLog();
        BeanUtils.copyProperties(source, target);
        operLogMapper.insert(target);
    }


}
