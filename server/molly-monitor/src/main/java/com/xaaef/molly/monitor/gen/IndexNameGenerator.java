package com.xaaef.molly.monitor.gen;

import com.xaaef.molly.tenant.props.MultiTenantProperties;
import com.xaaef.molly.common.util.TenantUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/4/21 18:25
 */

@Component
@AllArgsConstructor
public class IndexNameGenerator {

    private final MultiTenantProperties multiTenantProperties;

    public String getTenantId() {
        var tenantId = Optional.ofNullable(TenantUtils.getTenantId())
                .orElse(multiTenantProperties.getDefaultTenantId());
        return tenantId;
    }

}
