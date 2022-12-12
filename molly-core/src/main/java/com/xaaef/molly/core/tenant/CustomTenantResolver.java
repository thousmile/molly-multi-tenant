package com.xaaef.molly.core.tenant;

import com.xaaef.molly.core.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.core.tenant.props.MultiTenantProperties;
import com.xaaef.molly.core.tenant.util.TenantUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static org.hibernate.cfg.AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER;


/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/11/25 10:58
 */


@Slf4j
@Component
@AllArgsConstructor
public class CustomTenantResolver implements CurrentTenantIdentifierResolver,
        HibernatePropertiesCustomizer {

    private final MultiTenantProperties props;


    @Override
    public String resolveCurrentTenantIdentifier() {
        log.info("resolveCurrentTenantIdentifier {} ................", TenantUtils.getTenantId());
        // 如果当前是 master 库，直接放过。任何用户都可以进入 master 库，读取一些公共的数据，如: 全局配置，权限菜单
        if (StringUtils.equals(props.getDefaultTenantId(), TenantUtils.getTenantId())) {
            return TenantUtils.getTenantId();
        }
        // 如果当前已经登录了用户。如果是登录接口，就可以操作任何数据库
        if (JwtSecurityUtils.isAuthenticated()) {
            // 此用户是 master 库中的用户，就可以操作任何一个 租户的数据。
            // 否则就只能操作 自己 的数据库
            return StringUtils.equals(props.getDefaultTenantId(), JwtSecurityUtils.getTenantId()) ?
                    TenantUtils.getTenantId() :
                    JwtSecurityUtils.getTenantId();
        }
        return Optional.ofNullable(TenantUtils.getTenantId()).orElse(props.getDefaultTenantId());
    }


    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }


    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }


}
