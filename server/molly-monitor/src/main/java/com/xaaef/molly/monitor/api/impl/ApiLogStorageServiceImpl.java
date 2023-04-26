package com.xaaef.molly.monitor.api.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import com.xaaef.molly.common.consts.ESIndexName;
import com.xaaef.molly.common.util.BatchQueueUtils;
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
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.BulkOptions;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
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

    private final ElasticsearchTemplate esTemplate;

    private final BatchQueueUtils<LmsLoginLog> loginLogQueueHandler;

    private final BatchQueueUtils<LmsOperLog> operLogQueueHandler;


    public ApiLogStorageServiceImpl(ElasticsearchTemplate esTemplate) {
        this.esTemplate = esTemplate;
        var executorService = Executors.newFixedThreadPool(1);
        loginLogQueueHandler = BatchQueueUtils.bufferRate(
                100, Duration.ofSeconds(5), executorService,
                list -> {
                    var start = System.currentTimeMillis();
                    // 根据 索引名称 分组
                    list.stream()
                            .collect(Collectors.groupingBy(LmsLoginLog::getIndexName))
                            .forEach((indexName, values) -> {
                                esTemplate.save(values, IndexCoordinates.of(indexName));
                            });
                    var end = System.currentTimeMillis() - start;
                    log.info("批量新增登录日志 数量: {} , 耗时: {}ms", list.size(), end);
                });
        operLogQueueHandler = BatchQueueUtils.bufferRate(
                100, Duration.ofSeconds(5), executorService,
                list -> {
                    var start = System.currentTimeMillis();
                    // 根据 索引名称 分组
                    list.stream()
                            .collect(Collectors.groupingBy(LmsOperLog::getIndexName))
                            .forEach((indexName, values) -> {
                                esTemplate.save(values, IndexCoordinates.of(indexName));
                            });
                    var end = System.currentTimeMillis() - start;
                    log.info("批量新增操作日志 数量: {} , 耗时: {}ms", list.size(), end);
                });
    }


    @Override
    public void asyncLoginSave(LoginLogDTO source) {
        var target = new LmsLoginLog();
        BeanUtils.copyProperties(source, target);
        // 获取索引名称。格式: 租户ID_lms_login_log
        var indexName = ESIndexName.getLoginLogIndex();
        target.setIndexName(indexName);
        loginLogQueueHandler.add(target);
    }


    @Override
    public void asyncOperateSave(OperLogDTO source) {
        var target = new LmsOperLog();
        BeanUtils.copyProperties(source, target);
        target.setMethodArgs(JsonUtils.toJson(source.getMethodArgs()));
        target.setResponseResult(JsonUtils.toJson(source.getResponseResult()));
        // 获取索引名称。格式: 租户ID_lms_oper_log
        var indexName = ESIndexName.getOperateLogIndex();
        target.setIndexName(indexName);
        operLogQueueHandler.add(target);
    }


}
