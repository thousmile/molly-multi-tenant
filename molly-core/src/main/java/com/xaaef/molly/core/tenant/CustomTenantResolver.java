package com.xaaef.molly.core.tenant;

import com.xaaef.molly.common.util.RectRangeUtils;
import com.xaaef.molly.core.tenant.props.MultiTenantProperties;
import com.xaaef.molly.core.tenant.util.TenantUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;

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
@AllArgsConstructor
public class CustomTenantResolver implements CurrentTenantIdentifierResolver,
        HibernatePropertiesCustomizer {

    private final MultiTenantProperties props;

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = TenantUtils.getTenantId();
        return Optional.ofNullable(tenantId).orElse(props.getDefaultTenantId());
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
