package com.xaaef.molly.core.tenant.service.impl;

import com.xaaef.molly.core.redis.RedisCacheUtils;
import com.xaaef.molly.core.tenant.props.MultiTenantProperties;
import com.xaaef.molly.core.tenant.service.MultiTenantManager;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.xaaef.molly.core.tenant.consts.MbpConst.X_TENANT_ID;


@Slf4j
@Component
@AllArgsConstructor
public class RedisMultiTenantManager implements MultiTenantManager {

    private final RedisCacheUtils cacheUtils;

    private final MultiTenantProperties props;

    @Override
    public String getDefaultTenantId() {
        return props.getDefaultTenantId();
    }


    @Override
    public boolean existById(String tenantId) {
        return cacheUtils.hasHashKey(X_TENANT_ID, tenantId);
    }


    @Override
    public void addTenantId(String tenantId) {
        cacheUtils.setHashObject(X_TENANT_ID, tenantId, tenantId);
    }


    @Override
    public void addTenantIdBatch(Set<String> tenantIds) {
        tenantIds.forEach(this::addTenantId);
    }


    @Override
    public void removeTenantId(String tenantId) {
        cacheUtils.deleteHashKey(X_TENANT_ID, tenantId);
    }



    @PostConstruct
    public void init() {
        // 添加默认租户
        addTenantId(props.getDefaultTenantId());
    }


    @PreDestroy
    public void preDestroy() {
        cacheUtils.deleteKey(X_TENANT_ID);
        log.info("delete the tenantId in redis ...");
    }


}
