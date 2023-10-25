package com.xaaef.molly.corems.service;

import com.xaaef.molly.corems.entity.CmsProject;
import com.xaaef.molly.corems.vo.ResetPasswordVO;
import com.xaaef.molly.tenant.base.service.BaseService;


/**
 * <p>
 * 项目
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/9 15:33
 */

public interface CmsProjectService extends BaseService<CmsProject> {


    /**
     * 重置密码
     *
     * @param pwd
     * @return int
     * @date 2022/12/9 15:33
     */
    boolean resetPassword(ResetPasswordVO pwd);


}
