package com.xaaef.molly.system.runner;

import com.xaaef.molly.core.tenant.DataSourceManager;
import com.xaaef.molly.core.tenant.enums.DbStyle;
import com.xaaef.molly.core.tenant.service.MultiTenantManager;
import com.xaaef.molly.system.repository.SysTenantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;


/**
 * TODO 初始化 租户的表结构
 *
 * @author WangChenChen
 * @date 2022/12/7 21:20
 */


@Slf4j
@Component
@AllArgsConstructor
public class TenantTableRunner implements ApplicationRunner {

    private final SysTenantRepository tenantReps;

    private final DataSourceManager dataSourceManager;

    private final MultiTenantManager tenantManager;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var props = dataSourceManager.getMultiTenantProperties();
        // 添加默认租户
        tenantManager.addTenantId(props.getDefaultTenantId());
        long count = tenantReps.count();
        // 如果是 table 模式，就无需创建表结构。
        var flag = props.getCreateTable() && props.getDbStyle() != DbStyle.Table;
        int pageSize = 100;
        for (int i = 0; i < (count / pageSize); i++) {
            var pageRequest = PageRequest.of(i, pageSize);
            tenantReps.findAllByIncludeTenantId(pageRequest).stream()
                    .filter(tenantId -> !StringUtils.equals(tenantId, props.getDefaultTenantId()))
                    .forEach(tenantId -> {
                        tenantManager.addTenantId(tenantId);
                        if (flag) {
                            dataSourceManager.createTable(tenantId);
                        }
                    });
        }
    }


}
