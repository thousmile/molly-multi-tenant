package com.xaaef.molly.system.api.impl;

import com.xaaef.molly.internal.api.ApiEsIndexService;
import com.xaaef.molly.system.entity.DmsDeviceLog;
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
 * @date 2023/4/26 18:18
 */

@Slf4j
@Service
@AllArgsConstructor
public class ApiSystemEsIndexServiceImpl implements ApiEsIndexService {

    private final ElasticsearchTemplate template;

    @Override
    public void init(String tenantId) {
        DelegateUtils.delegate(tenantId, () -> {
            var index1 = template.indexOps(DmsDeviceLog.class);
            if (!index1.exists()) {
                var mapping = index1.createMapping(DmsDeviceLog.class);
                log.info("创建租户: {}  设备日志索引  {} ", tenantId, index1.create(Map.of(), mapping));
            } else {
                log.info("更新租户: {}  设备日志索引  {} ", tenantId, index1.putMapping(DmsDeviceLog.class));
            }
            return true;
        });
    }

}
