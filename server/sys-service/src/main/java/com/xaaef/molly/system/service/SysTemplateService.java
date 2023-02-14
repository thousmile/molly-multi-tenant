package com.xaaef.molly.system.service;

import com.xaaef.molly.tenant.base.service.BaseService;
import com.xaaef.molly.system.entity.SysTemplate;
import com.xaaef.molly.system.vo.UpdateMenusVO;

import java.util.Map;
import java.util.Set;


/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/9 15:33
 */


public interface SysTemplateService extends BaseService<SysTemplate> {

    /**
     * 获取权限模板的所有 菜单权限
     */
    UpdateMenusVO listHaveMenus(Long templateId);


    /**
     * 权限模板 修改菜单
     */
    boolean updateMenus(Long templateId, Set<Long> menus);


    /**
     * 根据租户ID, 获取关联的 模板
     */
    Map<String, Set<SysTemplate>> listByTenantIds(Set<String> tenantIds);


}
