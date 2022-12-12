package com.xaaef.molly.system.service.impl;

import cn.hutool.core.util.IdUtil;
import com.xaaef.molly.core.tenant.DataSourceManager;
import com.xaaef.molly.core.tenant.base.service.impl.BaseServiceImpl;
import com.xaaef.molly.core.tenant.service.MultiTenantManager;
import com.xaaef.molly.system.entity.SysTenant;
import com.xaaef.molly.system.repository.SysTenantRepository;
import com.xaaef.molly.system.service.SysConfigService;
import com.xaaef.molly.system.service.SysTenantService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.xaaef.molly.common.consts.ConfigName.TENANT_DEFAULT_LOGO;
import static com.xaaef.molly.core.auth.jwt.JwtSecurityUtils.isAdminUser;
import static com.xaaef.molly.core.auth.jwt.JwtSecurityUtils.isMasterUser;

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

    private final SysConfigService configService;


    /**
     * uuid
     */
    public static String getUUIDSuffix() {
        return IdUtil.fastUUID().split("-")[4];
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysTenant save(SysTenant entity) {
        if (!isMasterUser()) {
            throw new RuntimeException("只有系统用户，才能创建租户！");
        }
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
            entity.setExpired(LocalDateTime.now().plusYears(10));
        }
        if (entity.getStatus() == null) {
            entity.setStatus((byte) 0);
        }
        if (entity.getLogo() == null) {
            var defaultLogoPath = configService.findValueByKey(TENANT_DEFAULT_LOGO);
            entity.setLogo(defaultLogoPath);
        }
        var save = super.save(entity);
        // 将 新创建的 租户ID 保存到 redis 中
        tenantManager.addTenantId(entity.getTenantId());
        // 新创建的 租户 创建表结构
        dataSourceManager.createTable(entity.getTenantId());
        return save;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        if (isMasterUser() && isAdminUser()) {
            if (!existsById(id)) {
                return;
            }
            super.deleteById(id);
            tenantManager.removeTenantId(id);
            dataSourceManager.deleteTable(id);
        } else {
            throw new RuntimeException("非系统管理员，无法删除租户！");
        }
    }


}
