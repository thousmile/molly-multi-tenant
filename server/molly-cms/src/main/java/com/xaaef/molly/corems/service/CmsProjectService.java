package com.xaaef.molly.corems.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xaaef.molly.common.po.SearchPO;
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


    /**
     * 分页查询项目简单字段
     *
     * @param po
     * @author WangChenChen
     * @version 2.0
     * @date 2023/10/25 17:22
     */
    IPage<CmsProject> simplePageKeywords(SearchPO po);


}
