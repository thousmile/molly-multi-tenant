package com.xaaef.molly.internal.api;

import com.xaaef.molly.internal.dto.InitUserDTO;
import com.xaaef.molly.internal.dto.PmsUserDTO;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/2/14 10:17
 */

public interface ApiPmsUserService {


    /**
     * 根据 用户名 获取用户信息
     *
     * @author WangChenChen
     * @date 2023/2/14 10:53
     */
    PmsUserDTO getByUsername(String username);


    /**
     * 根据 手机号 获取用户信息
     *
     * @author WangChenChen
     * @date 2023/2/14 10:53
     */
    PmsUserDTO getByMobile(String mobile);


    /**
     * 根据 邮箱 获取用户信息
     *
     * @author WangChenChen
     * @date 2023/2/14 10:53
     */
    PmsUserDTO getByEmail(String email);


    /**
     * 根据 用户名 获取用户信息
     *
     * @author WangChenChen
     * @date 2023/2/14 10:53
     */
    Set<PmsUserDTO> listByUserIds(Set<Long> userIds);


    /**
     * 根据 用户Id 判断是否存在
     *
     * @author WangChenChen
     * @date 2023/2/14 10:53
     */
    boolean existByUserId(Long userId);


    /**
     * 创建租户  初始化用户
     *
     * @author WangChenChen
     * @date 2023/2/14 10:53
     */
    void initUserAndRoleAndDept(InitUserDTO po);

}
