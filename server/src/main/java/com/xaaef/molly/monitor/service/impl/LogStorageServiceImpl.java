package com.xaaef.molly.monitor.service.impl;

import com.xaaef.molly.common.util.JsonUtils;
import com.xaaef.molly.core.log.domain.LoginLog;
import com.xaaef.molly.core.log.domain.OperLog;
import com.xaaef.molly.core.log.service.LogStorageService;
import com.xaaef.molly.monitor.entity.LmsLoginLog;
import com.xaaef.molly.monitor.entity.LmsOperLog;
import com.xaaef.molly.monitor.mapper.LmsLoginLogMapper;
import com.xaaef.molly.monitor.mapper.LmsOperLogMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * <p>
 * 日志
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/8/17 16:18
 */


@Slf4j
@Component
@AllArgsConstructor
public class LogStorageServiceImpl implements LogStorageService {

    private final LmsLoginLogMapper loginLogMapper;

    private final LmsOperLogMapper operLogMapper;


    @Async
    @Override
    public void asyncLoginSave(LoginLog loginLog) {
        var target = new LmsLoginLog();
        BeanUtils.copyProperties(loginLog, target);
        loginLogMapper.insert(target);
        // log.info("登录日志: \n{}", JsonUtils.toFormatJson(target));
    }


    @Async
    @Override
    public void asyncOperateSave(OperLog operLog) {
        var target = new LmsOperLog();
        BeanUtils.copyProperties(operLog, target);
        target.setResponseResult(JsonUtils.toJson(operLog.getResponseResult()));
        target.setMethodArgs(JsonUtils.toJson(operLog.getMethodArgs()));
        operLogMapper.insert(target);
        // log.info("操作日志: \n{}", JsonUtils.toFormatJson(target));
    }


}
