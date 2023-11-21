package com.xaaef.molly.system.runner;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaaef.molly.common.consts.MbpConst;
import com.xaaef.molly.common.domain.SmallTenant;
import com.xaaef.molly.common.util.JsonUtils;
import com.xaaef.molly.internal.api.ApiCmsProjectService;
import com.xaaef.molly.system.entity.SysTenant;
import com.xaaef.molly.system.mapper.SysTenantMapper;
import com.xaaef.molly.tenant.DatabaseManager;
import com.xaaef.molly.tenant.service.MultiTenantManager;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


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
@DependsOn(value = "projectTableRunner")
@Order(Integer.MIN_VALUE + 1)
@AllArgsConstructor
public class TenantTableRunner implements ServletContextListener {

    private final SysTenantMapper tenantMapper;

    private final DatabaseManager databaseManager;

    private final MultiTenantManager tenantManager;

    private final ApiCmsProjectService projectService;


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("TenantTableRunner Initialized .....");
        var props = databaseManager.getMultiTenantProperties();
        if (props.getEnable()) {
            log.info("ignore tenant_id intercept table name : \n{}", JsonUtils.toJson(MbpConst.TENANT_IGNORE_TABLES));
            // 获取默认租户Id
            var defaultTenantId = props.getDefaultTenantId();
            var wrapper = new LambdaQueryWrapper<SysTenant>()
                    .select(List.of(SysTenant::getTenantId, SysTenant::getLogo,
                            SysTenant::getName, SysTenant::getLinkman));
            var smallTenantList = tenantMapper.selectList(wrapper)
                    .stream()
                    .map(source -> BeanUtil.copyProperties(source, SmallTenant.class))
                    .collect(Collectors.toSet());

            var tenantIds = smallTenantList.stream()
                    .map(SmallTenant::getTenantId)
                    .collect(Collectors.toSet());

            // 是否创建表结构
            if (props.getCreateTable()) {
                var newTenantIds = new HashSet<>(tenantIds);
                newTenantIds.remove(defaultTenantId);
                try {
                    databaseManager.updateTable(newTenantIds);
                } catch (Exception e) {
                    log.error("updateTable error: \n{}", e.getMessage());
                }
            }

            var tenantProjectMaps = projectService.mapByTenantDbName(props.getPrefix(), tenantIds);

            smallTenantList.forEach(t -> {
                t.setProjectIds(new HashSet<>());
                if (tenantProjectMaps.containsKey(t.getTenantId())) {
                    var projectIds = tenantProjectMaps.get(t.getTenantId());
                    t.getProjectIds().addAll(projectIds);
                }
            });

            tenantManager.addTenantIdBatch(smallTenantList);
        }
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        tenantManager.removeAll();
        log.info("delete the tenantId in redis ...");
    }

}
