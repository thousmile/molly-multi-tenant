package com.xaaef.molly.system.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaaef.molly.common.util.TenantUtils;
import com.xaaef.molly.internal.api.ApiSysTenantService;
import com.xaaef.molly.internal.dto.SysTenantDTO;
import com.xaaef.molly.system.entity.SysTenant;
import com.xaaef.molly.system.mapper.SysTenantMapper;
import com.xaaef.molly.tenant.service.MultiTenantManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/2/14 11:38
 */


@Slf4j
@Service
@AllArgsConstructor
public class ApiSysTenantServiceImpl implements ApiSysTenantService {

    private final SysTenantMapper tenantMapper;

    private final MultiTenantManager tenantManager;

    @Override
    public SysTenantDTO getByTenantId(String tenantId) {
        var source = tenantMapper.selectById(tenantId);
        if (source == null) {
            return null;
        }
        var target = new SysTenantDTO();
        BeanUtils.copyProperties(source, target);
        return target;
    }


    @Override
    public SysTenantDTO getSimpleByTenantId(String tenantId) {
        var wrapper = new LambdaQueryWrapper<SysTenant>()
                .select(
                        SysTenant::getTenantId,
                        SysTenant::getLogo,
                        SysTenant::getName,
                        SysTenant::getLinkman
                ).eq(SysTenant::getTenantId, tenantId);
        var source = tenantMapper.selectOne(wrapper);
        if (source == null) {
            return null;
        }
        var target = new SysTenantDTO();
        BeanUtils.copyProperties(source, target);
        return target;
    }


    @Override
    public SysTenantDTO getByCurrentTenant() {
        return getByTenantId(TenantUtils.getTenantId());
    }


    @Override
    public String getByDefaultTenantId() {
        return tenantManager.getDefaultTenantId();
    }


}
