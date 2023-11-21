package com.xaaef.molly.tenant.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.xaaef.molly.common.domain.SmallTenant;
import com.xaaef.molly.tenant.props.MultiTenantProperties;
import com.xaaef.molly.tenant.service.MultiTenantManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xaaef.molly.common.util.TenantUtils.X_TENANT_ID;


@Slf4j
@Component
@AllArgsConstructor
public class RedisMultiTenantManager implements MultiTenantManager {

    private final RedisTemplate<String, Object> redisTemplate;


    private final MultiTenantProperties multiTenantProperties;


    @Override
    public String getDefaultTenantId() {
        return multiTenantProperties.getDefaultTenantId();
    }


    @Override
    public boolean isDefaultTenantId(String tenantId) {
        return StringUtils.equals(multiTenantProperties.getDefaultTenantId(), tenantId);
    }


    @Override
    public boolean existById(String tenantId) {
        return redisTemplate.opsForHash().hasKey(X_TENANT_ID, tenantId);
    }


    @Override
    public SmallTenant getById(String tenantId) {
        var obj = redisTemplate.opsForHash().get(X_TENANT_ID, tenantId);
        if (obj == null) {
            return null;
        }
        return BeanUtil.copyProperties(obj, SmallTenant.class);
    }


    @Override
    public void addTenantId(SmallTenant tenant) {
        addTenantIdBatch(Set.of(tenant));
    }


    @Override
    public void addTenantIdBatch(Set<SmallTenant> tenants) {
        Map<String, SmallTenant> tenantMaps = tenants.stream()
                .collect(Collectors.toMap(SmallTenant::getTenantId, t -> t));
        redisTemplate.opsForHash().putAll(X_TENANT_ID, tenantMaps);
    }


    @Override
    public void removeTenantId(String tenantId) {
        redisTemplate.opsForHash().delete(X_TENANT_ID, tenantId);
    }


    @Override
    public void removeAll() {
        redisTemplate.delete(X_TENANT_ID);
    }


}
