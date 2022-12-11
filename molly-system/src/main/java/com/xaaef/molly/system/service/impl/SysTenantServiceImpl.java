package com.xaaef.molly.system.service.impl;

import cn.hutool.core.util.IdUtil;
import com.xaaef.molly.core.tenant.DataSourceManager;
import com.xaaef.molly.core.tenant.base.service.impl.BaseServiceImpl;
import com.xaaef.molly.core.tenant.service.MultiTenantManager;
import com.xaaef.molly.system.entity.SysTenant;
import com.xaaef.molly.system.repository.SysTenantRepository;
import com.xaaef.molly.system.service.SysTenantService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/9 15:36
 */


@Slf4j
@Service
@AllArgsConstructor
public class SysTenantServiceImpl extends BaseServiceImpl<SysTenantRepository, SysTenant, String>
        implements SysTenantService {

    private final MultiTenantManager tenantManager;

    private final DataSourceManager dataSourceManager;

    /**
     * uuid
     */
    public static String getUUIDSuffix() {
        return IdUtil.fastUUID().split("-")[4];
    }


    @Override
    public SysTenant save(SysTenant entity) {
        if (StringUtils.isBlank(entity.getTenantId())) {
            do {
                entity.setTenantId(getUUIDSuffix());
            }
            while (tenantManager.existById(entity.getTenantId()));
        } else {
            if (tenantManager.existById(entity.getTenantId())) {
                throw new RuntimeException(String.format("租户ID %s 已经存在了！", entity.getTenantId()));
            }
        }
        if (entity.getExpired() == null) {
            entity.setExpired(LocalDateTime.now().plusYears(100));
        }
        if (entity.getStatus() == null) {
            entity.setStatus((byte) 0);
        }
        var save = super.save(entity);
        // 将 新创建的 租户ID 保存到 redis 中
        tenantManager.addTenantId(entity.getTenantId());
        // 新创建的 租户 创建表结构
        dataSourceManager.createTable(entity.getTenantId());
        return save;
    }


}
