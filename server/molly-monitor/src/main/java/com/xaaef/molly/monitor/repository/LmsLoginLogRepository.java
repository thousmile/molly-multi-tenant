package com.xaaef.molly.monitor.repository;

import com.xaaef.molly.monitor.entity.LmsLoginLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * <p>
 * 登录日志
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/4/21 18:11
 */

public interface LmsLoginLogRepository extends ElasticsearchRepository<LmsLoginLog, String> {



}
