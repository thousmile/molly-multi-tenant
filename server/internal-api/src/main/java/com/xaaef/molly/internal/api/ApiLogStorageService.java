package com.xaaef.molly.internal.api;

import com.xaaef.molly.internal.dto.LoginLogDTO;
import com.xaaef.molly.internal.dto.OperLogDTO;

/**
 * <p>
 * 运行 日志 存储
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/2/14 10:18
 */

public interface ApiLogStorageService {


    /**
     * 异步保存 登录日志 到 ES 中
     *
     * @param loginLog
     * @author Wang Chen Chen
     * @date 2021/8/11 15:30
     */
    void asyncLoginSave(LoginLogDTO loginLog);


    /**
     * 异步保存 操作日志 到 ES 中
     *
     * @param operLog
     * @author Wang Chen Chen
     * @date 2021/8/11 15:30
     */
    void asyncOperateSave(OperLogDTO operLog);

}
