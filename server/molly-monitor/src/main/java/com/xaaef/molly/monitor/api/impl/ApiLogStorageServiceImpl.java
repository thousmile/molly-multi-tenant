package com.xaaef.molly.monitor.api.impl;

import cn.hutool.core.bean.BeanUtil;
import com.xaaef.molly.common.util.JsonUtils;
import com.xaaef.molly.internal.api.ApiLogStorageService;
import com.xaaef.molly.internal.dto.LoginLogDTO;
import com.xaaef.molly.internal.dto.OperLogDTO;
import com.xaaef.molly.monitor.entity.LmsLoginLog;
import com.xaaef.molly.monitor.entity.LmsOperLog;
import com.xaaef.molly.monitor.repository.LmsLoginLogRepository;
import com.xaaef.molly.monitor.repository.LmsOperLogRepository;
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

    private final LmsLoginLogRepository loginLogService;

    private final LmsOperLogRepository operLogService;


    @Override
    public void asyncLoginSave(LoginLogDTO source) {
        var target = new LmsLoginLog();
        BeanUtils.copyProperties(source, target);
        loginLogService.save(target);
    }


    @Override
    public void asyncOperateSave(OperLogDTO source) {
        var target = new LmsOperLog();
        BeanUtils.copyProperties(source, target);
        target.setMethodArgs(JsonUtils.toJson(source.getMethodArgs()));
        target.setResponseResult(JsonUtils.toJson(source.getResponseResult()));
        operLogService.save(target);
    }


}
