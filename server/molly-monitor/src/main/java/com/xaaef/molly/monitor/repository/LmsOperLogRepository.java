package com.xaaef.molly.monitor.repository;

import com.xaaef.molly.monitor.entity.LmsOperLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * <p>
 * 操作日志
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/4/21 18:11
 */

public interface LmsOperLogRepository extends ElasticsearchRepository<LmsOperLog, String> {

    

}
