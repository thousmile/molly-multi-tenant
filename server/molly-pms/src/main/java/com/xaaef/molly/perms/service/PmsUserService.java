package com.xaaef.molly.perms.service;

import com.xaaef.molly.auth.jwt.JwtLoginUser;
import com.xaaef.molly.tenant.base.service.BaseService;
import com.xaaef.molly.perms.entity.PmsUser;
import com.xaaef.molly.perms.vo.ResetPasswordVO;
import com.xaaef.molly.perms.vo.UpdatePasswordVO;
import com.xaaef.molly.perms.vo.UserRightsVO;

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
     * TODO 在线的所有用户
     *
     * @author WangChenChen
     * @date 2023/4/22 8:50
     */
    Set<JwtLoginUser> listLoginUsers();


    /**
     * 修改密码
     *
     * @param pwd
     * @return int
     * @date 2022/12/9 15:33
     */
    boolean updatePassword(UpdatePasswordVO pwd);


    /**
     * 重置密码
     *
     * @param pwd
     * @return int
     * @date 2022/12/9 15:33
     */
    boolean resetPassword(ResetPasswordVO pwd);


    /**
     * 修改用户的角色
     *
     * @param userId
     * @param roleIds
     * @return int
     * @date 2022/12/9 15:33
     */
    Boolean updateUserRoles(Long userId, Set<Long> roleIds);


    /**
     * 获取用户权限
     */
    UserRightsVO getUserRights();


}
