package com.xaaef.molly.monitor.api.impl;

import com.xaaef.molly.internal.api.ApiEsIndexService;
import com.xaaef.molly.monitor.entity.LmsLoginLog;
import com.xaaef.molly.monitor.entity.LmsOperLog;
import com.xaaef.molly.tenant.util.DelegateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 初始化
 * 登录日志，操作日志 的 es 索引
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/4/26 18:14
 */


@Slf4j
@Service
@AllArgsConstructor
public class ApiMonitorEsIndexServiceImpl implements ApiEsIndexService {

    private final ElasticsearchTemplate template;

    @Override
    public void init(String tenantId) {
        DelegateUtils.delegate(tenantId, () -> {
            var index1 = template.indexOps(LmsLoginLog.class);
            if (!index1.exists()) {
                var mapping = index1.createMapping(LmsLoginLog.class);
                log.info("创建租户: {}  登录日志索引  {} ", tenantId, index1.create(Map.of(), mapping));
            }
            var index2 = template.indexOps(LmsOperLog.class);
            if (!index2.exists()) {
                var mapping = index2.createMapping(LmsOperLog.class);
                log.info("创建租户: {}  操作日志索引  {} ", tenantId, index2.create(Map.of(), mapping));
            }
            return true;
        });
    }

}
