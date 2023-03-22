package com.xaaef.molly.monitor.api.impl;

import com.xaaef.molly.common.util.BatchQueueUtils;
import com.xaaef.molly.internal.api.ApiLogStorageService;
import com.xaaef.molly.internal.dto.LoginLogDTO;
import com.xaaef.molly.internal.dto.OperLogDTO;
import com.xaaef.molly.monitor.entity.LmsLoginLog;
import com.xaaef.molly.monitor.entity.LmsOperLog;
import com.xaaef.molly.monitor.mapper.LmsLoginLogMapper;
import com.xaaef.molly.monitor.mapper.LmsOperLogMapper;
import com.xaaef.molly.monitor.service.LmsLoginLogService;
import com.xaaef.molly.monitor.service.LmsOperLogService;
import com.xaaef.molly.tenant.util.DelegateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    private final LmsLoginLogService loginLogService;

    private final LmsOperLogService operLogService;

    private final BatchQueueUtils<LoginLogDTO> queueHandler1;

    private final BatchQueueUtils<OperLogDTO> queueHandler2;

    public ApiLogStorageServiceImpl(LmsLoginLogService loginLogService, LmsOperLogService operLogService) {
        this.loginLogService = loginLogService;
        this.operLogService = operLogService;
        var es = Executors.newSingleThreadExecutor();
        // 批量 登录日志
        this.queueHandler1 = BatchQueueUtils.bufferRate(
                100, Duration.ofSeconds(5), es,
                list -> {
                    list.stream()
                            .collect(Collectors.groupingBy(
                                            LoginLogDTO::getTenantId,
                                            Collectors.mapping(source -> {
                                                        var target = new LmsLoginLog();
                                                        BeanUtils.copyProperties(source, target);
                                                        return target;
                                                    },
                                                    Collectors.toSet())
                                    )
                            ).forEach((tenantId, collect) -> {
                                var start = System.currentTimeMillis();
                                var flag = DelegateUtils.delegate(tenantId,
                                        () -> loginLogService.saveBatch(collect)
                                );
                                var end = System.currentTimeMillis() - start;
                                log.info("批量新增[登录日志]: {} => 数量: {} => 耗时: {} ms", flag, list.size(), end);
                            });
                });

        // 批量 操作日志
        this.queueHandler2 = BatchQueueUtils.bufferRate(
                200, Duration.ofSeconds(5), es,
                list -> {
                    list.stream()
                            .collect(Collectors.groupingBy(
                                            OperLogDTO::getTenantId,
                                            Collectors.mapping(source -> {
                                                        var target = new LmsOperLog();
                                                        BeanUtils.copyProperties(source, target);
                                                        return target;
                                                    },
                                                    Collectors.toSet())
                                    )
                            ).forEach((tenantId, collect) -> {
                                var start = System.currentTimeMillis();
                                var flag = DelegateUtils.delegate(tenantId,
                                        () -> operLogService.saveBatch(collect)
                                );
                                var end = System.currentTimeMillis() - start;
                                log.info("批量新增[操作日志]: {} => 数量: {} => 耗时: {} ms", flag, list.size(), end);
                            });
                });
    }


    @Override
    public void asyncLoginSave(LoginLogDTO source) {
        /// queueHandler1.add(source);
        var target = new LmsLoginLog();
        BeanUtils.copyProperties(source, target);
        loginLogService.save(target);
    }


    @Override
    public void asyncOperateSave(OperLogDTO source) {
        // queueHandler2.add(source);
        var target = new LmsOperLog();
        BeanUtils.copyProperties(source, target);
        operLogService.save(target);
    }


}
