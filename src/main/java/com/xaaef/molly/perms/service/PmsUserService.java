package com.xaaef.molly.perms.service;

import com.xaaef.molly.core.tenant.base.service.BaseService;
import com.xaaef.molly.perms.entity.PmsUser;
import com.xaaef.molly.perms.vo.ResetPasswordVO;
import com.xaaef.molly.perms.vo.UpdatePasswordVO;

import java.util.Set;


/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/9 15:33
 */

public interface PmsUserService extends BaseService<PmsUser> {



    /**
     * 修改密码
     *
     * @param pwd
     * @return int
     * @author Wang Chen Chen<932560435@qq.com>
     * @date 2019/12/12 21:20
     */
    boolean updatePassword(UpdatePasswordVO pwd);


    /**
     * 重置密码
     *
     * @param pwd
     * @return int
     * @author Wang Chen Chen<932560435@qq.com>
     * @date 2019/12/12 21:20
     */
    boolean resetPassword(ResetPasswordVO pwd);


    /**
     * 修改用户的角色
     *
     * @param userId
     * @param roleIds
     * @return int
     * @author Wang Chen Chen<932560435@qq.com>
     * @date 2019/12/12 21:20
     */
    int updateUserRoles(Long userId, Set<Long> roleIds);


}
