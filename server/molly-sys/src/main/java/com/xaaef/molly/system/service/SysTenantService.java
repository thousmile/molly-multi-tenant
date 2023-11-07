package com.xaaef.molly.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.system.entity.SysTenant;
import com.xaaef.molly.system.po.CreateTenantPO;
import com.xaaef.molly.system.po.TenantCreatedSuccessVO;
import com.xaaef.molly.system.vo.TenantSimpleDataVO;
import com.xaaef.molly.tenant.base.service.BaseService;

import java.util.Collection;
import java.util.Set;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/9 15:33
 */

public interface SysTenantService extends BaseService<SysTenant> {

    /**
     * 根据关键字查询
     *
     * @author Wang Chen Chen
     * @date 2021/8/25 9:41
     */
    IPage<SysTenant> pageKeywords(SearchPO params);


    /**
     * 根据关键字查询
     *
     * @author Wang Chen Chen
     * @date 2021/8/25 9:41
     */
    IPage<SysTenant> simplePageKeywords(SearchPO params);


    Collection<TenantSimpleDataVO> simpleSearchQuery(SearchPO po);


    /**
     * 创建商户
     *
     * @param params
     * @return
     * @author Wang Chen Chen
     * @date 2021/7/16 17:31
     */
    TenantCreatedSuccessVO create(CreateTenantPO po);


    /**
     * 修改 租户权限模板
     *
     * @param tenantId
     * @param templateId
     * @return
     * @author Wang Chen Chen
     * @date 2021/7/16 17:31
     */
    boolean updateTemplate(String tenantId, Set<Long> templateIds);

}
