package com.xaaef.molly.internal.api;

import com.xaaef.molly.internal.dto.OperateUserDTO;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 反射 填充 操作用户信息
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/10/31 10:53
 */

public interface ApiOperateUserService {

    /**
     * 根据 用户ID 获取 操作用户
     */
    void reflectionFill(Object objList);


    /**
     * 根据 用户ID 获取 操作用户
     */
    Map<Long, OperateUserDTO> mapOperateUser(Set<Long> userIds);

}
