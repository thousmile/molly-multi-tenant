package com.xaaef.molly.monitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.monitor.entity.LmsOperLog;

import java.util.Set;

/**
 * <p>
 * 操作日志
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/2/6 15:28
 */

public interface LmsOperLogService {


    IPage<LmsOperLog> pageKeywords(SearchPO params);


    /**
     * 批量删除
     *
     * @author WangChenChen
     * @date 2023/4/21 18:14
     */
    boolean removeBatchByIds(Set<String> ids);


}
