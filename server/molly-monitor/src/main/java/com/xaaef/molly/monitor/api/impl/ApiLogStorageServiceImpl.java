package com.xaaef.molly.monitor.api.impl;

import com.xaaef.molly.common.util.BatchQueueUtils;
import com.xaaef.molly.common.util.JsonUtils;
import com.xaaef.molly.internal.api.ApiLogStorageService;
import com.xaaef.molly.internal.dto.LoginLogDTO;
import com.xaaef.molly.internal.dto.OperLogDTO;
import com.xaaef.molly.monitor.entity.LmsLoginLog;
import com.xaaef.molly.monitor.entity.LmsOperLog;
import com.xaaef.molly.monitor.service.LmsLoginLogService;
import com.xaaef.molly.monitor.service.LmsOperLogService;
import com.xaaef.molly.tenant.util.DelegateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


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
public class ApiLogStorageServiceImpl implements ApiLogStorageService {

    private final BatchQueueUtils<LoginLogDTO> loginLogQueueHandler;

    private final BatchQueueUtils<OperLogDTO> operLogQueueHandler;


    public ApiLogStorageServiceImpl(LmsLoginLogService loginLogService, LmsOperLogService operLogService) {
        var executorService = Executors.newFixedThreadPool(1);
        loginLogQueueHandler = BatchQueueUtils.bufferRate(
                100, Duration.ofSeconds(10), executorService,
                list -> {
                    var start = System.currentTimeMillis();
                    // 根据 索引名称 分组
                    list.stream()
                            .collect(Collectors.groupingBy(LoginLogDTO::getTenantId))
                            .forEach((tenantId, values) -> {
                                var collect = values.stream()
                                        .map(source -> {
                                            var target = new LmsLoginLog();
                                            BeanUtils.copyProperties(source, target);
                                            return target;
                                        }).collect(Collectors.toSet());
                                if (!collect.isEmpty()) {
                                    DelegateUtils.delegate(tenantId, () -> loginLogService.saveBatch(collect));
                                }
                            });
                    var end = System.currentTimeMillis() - start;
                    log.info("批量保存登录日志 数量: {} , 耗时: {} ms", list.size(), end);
                });

        operLogQueueHandler = BatchQueueUtils.bufferRate(
                100, Duration.ofSeconds(10), executorService,
                list -> {
                    var start = System.currentTimeMillis();
                    // 根据 索引名称 分组
                    list.stream()
                            .collect(Collectors.groupingBy(OperLogDTO::getTenantId))
                            .forEach((tenantId, values) -> {
                                var collect = values.stream().map(source -> {
                                    var target = new LmsOperLog();
                                    BeanUtils.copyProperties(source, target);
                                    target.setMethodArgs(JsonUtils.toJson(source.getMethodArgs()));
                                    target.setResponseResult(JsonUtils.toJson(source.getResponseResult()));
                                    return target;
                                }).collect(Collectors.toSet());
                                if (!collect.isEmpty()) {
                                    DelegateUtils.delegate(tenantId, () -> operLogService.saveBatch(collect));
                                }
                            });
                    var end = System.currentTimeMillis() - start;
                    log.info("批量保存操作日志 数量: {} , 耗时: {}ms", list.size(), end);
                });
    }


    @Override
    public void asyncLoginSave(LoginLogDTO source) {
        loginLogQueueHandler.add(source);
    }


    @Override
    public void asyncOperateSave(OperLogDTO source) {
        operLogQueueHandler.add(source);
    }


}
