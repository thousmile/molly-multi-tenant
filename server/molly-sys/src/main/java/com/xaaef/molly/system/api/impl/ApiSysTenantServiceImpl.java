package com.xaaef.molly.system.api.impl;

import com.xaaef.molly.common.domain.SmallTenant;
import com.xaaef.molly.common.util.TenantUtils;
import com.xaaef.molly.internal.api.ApiSysTenantService;
import com.xaaef.molly.internal.dto.MultiTenantPropertiesDTO;
import com.xaaef.molly.internal.dto.SysTenantDTO;
import com.xaaef.molly.system.mapper.SysTenantMapper;
import com.xaaef.molly.tenant.props.MultiTenantProperties;
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

    private final MultiTenantProperties multiTenantProperties;


    @Override
    public boolean existById(String tenantId) {
        return tenantManager.existById(tenantId);
    }


    @Override
    public boolean existTenantIncludeProjectId(String tenantId, Long projectId) {
        var st = getSmallByTenantId(tenantId);
        if (st == null) {
            return false;
        }
        return st.getProjectIds().contains(projectId);
    }


    @Override
    public void tenantAddProjectId(String tenantId, Long projectId) {
        var st = getSmallByTenantId(tenantId);
        if (st == null) {
            return;
        }
        st.getProjectIds().add(projectId);
        tenantManager.addTenantId(st);
    }


    @Override
    public void tenantDelProjectId(String tenantId, Long projectId) {
        var st = getSmallByTenantId(tenantId);
        if (st == null) {
            return;
        }
        st.getProjectIds().remove(projectId);
        tenantManager.addTenantId(st);
    }


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
    public SmallTenant getSmallByTenantId(String tenantId) {
        return tenantManager.getById(tenantId);
    }


    @Override
    public SysTenantDTO getByCurrentTenant() {
        return getByTenantId(TenantUtils.getTenantId());
    }


    @Override
    public String getByDefaultTenantId() {
        return multiTenantProperties.getDefaultTenantId();
    }


    @Override
    public MultiTenantPropertiesDTO getByMultiTenantProperties() {
        var target = new MultiTenantPropertiesDTO();
        BeanUtils.copyProperties(multiTenantProperties, target);
        return target;
    }


}
