package com.xaaef.molly.system.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xaaef.molly.core.tenant.DatabaseManager;
import com.xaaef.molly.core.tenant.enums.DbStyle;
import com.xaaef.molly.core.tenant.service.MultiTenantManager;
import com.xaaef.molly.perms.entity.PmsUser;
import com.xaaef.molly.perms.mapper.PmsRoleMapper;
import com.xaaef.molly.perms.mapper.PmsUserMapper;
import com.xaaef.molly.system.entity.SysTenant;
import com.xaaef.molly.system.mapper.SysTenantMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;


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

    private final SysTenantMapper tenantMapper;

    private final DatabaseManager databaseManager;

    private final MultiTenantManager tenantManager;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var props = databaseManager.getMultiTenantProperties();
        long count = tenantMapper.selectCount(null);
        // 如果是 table 模式，就无需创建表结构。
        var flag = props.getCreateTable() && props.getDbStyle() != DbStyle.Table;
        int pageSize = 100;
        long pageCount = (count / pageSize) + 1;
        for (int i = 1; i <= pageCount; i++) {
            Page<SysTenant> pageRequest = Page.of(i, pageSize);
            var wrapper = new LambdaQueryWrapper<SysTenant>()
                    .select(SysTenant::getTenantId);
            tenantMapper.selectPage(pageRequest, wrapper)
                    .getRecords()
                    .stream()
                    .map(SysTenant::getTenantId)
                    .filter(tenantId -> !StringUtils.equals(tenantId, props.getDefaultTenantId()))
                    .forEach(tenantId -> {
                        tenantManager.addTenantId(tenantId);
                        if (flag) {
                            databaseManager.updateTable(tenantId);
                        }
                    });
        }
    }


}
