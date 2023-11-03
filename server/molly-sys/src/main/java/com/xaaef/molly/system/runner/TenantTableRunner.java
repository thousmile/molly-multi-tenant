package com.xaaef.molly.system.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xaaef.molly.common.consts.MbpConst;
import com.xaaef.molly.common.util.JsonUtils;
import com.xaaef.molly.system.entity.SysTenant;
import com.xaaef.molly.system.mapper.SysTenantMapper;
import com.xaaef.molly.tenant.DatabaseManager;
import com.xaaef.molly.tenant.service.MultiTenantManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * TODO 初始化 租户的表结构
 * <p>
 *     一定要在
 *     @see com.xaaef.molly.corems.runner.ProjectTableRunner.run()
 *     方法之后初始化
 * </p>
 *
 * @author WangChenChen
 * @date 2022/12/7 21:20
 * <p>
 */


@Slf4j
@Component
@Order(Integer.MIN_VALUE + 10)
@AllArgsConstructor
public class TenantTableRunner implements ApplicationRunner {

    private final SysTenantMapper tenantMapper;

    private final DatabaseManager databaseManager;

    private final MultiTenantManager tenantManager;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var props = databaseManager.getMultiTenantProperties();
        if (props.getEnable()) {
            log.info("Execute TenantTableRunner run() ...");
            log.info("ignore tenant_id intercept table name : \n{}", JsonUtils.toJson(MbpConst.TENANT_IGNORE_TABLES));
            // 获取默认租户Id
            var defaultTenantId = props.getDefaultTenantId();
            // 是否创建表结构
            var createTable = props.getCreateTable();
            var count = tenantMapper.selectCount(null);
            var pageSize = 100;
            var pageCount = (count / pageSize) + 1;
            for (int i = 1; i <= pageCount; i++) {
                Page<SysTenant> pageRequest = Page.of(i, pageSize);
                var wrapper = new LambdaQueryWrapper<SysTenant>()
                        .select(List.of(SysTenant::getTenantId));
                tenantMapper.selectPage(pageRequest, wrapper)
                        .getRecords()
                        .stream()
                        .map(SysTenant::getTenantId)
                        .filter(tenantId -> !StringUtils.equals(tenantId, defaultTenantId))
                        .forEach(tenantId -> {
                            tenantManager.addTenantId(tenantId);
                            if (createTable) {
                                databaseManager.updateTable(tenantId);
                            }
                        });
            }
        }
    }


}
