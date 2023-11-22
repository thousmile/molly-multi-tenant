package com.xaaef.molly.corems.runner;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xaaef.molly.common.consts.MbpConst;
import com.xaaef.molly.common.util.JsonUtils;
import com.xaaef.molly.corems.entity.CmsProject;
import com.xaaef.molly.corems.mapper.CmsProjectMapper;
import com.xaaef.molly.tenant.props.MultiTenantProperties;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.xaaef.molly.common.consts.MbpConst.PROJECT_ID;


/**
 * 初始化 数据库中 包含 项目id 的 数据库表名称
 *
 * @author WangChenChen
 * @date 2022/12/7 21:20
 */


@Slf4j
@Component
@Order(Integer.MIN_VALUE)
@AllArgsConstructor
public class ProjectTableRunner implements ServletContextListener {

    private final CmsProjectMapper projectMapper;

    private final MultiTenantProperties multiTenantProperties;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("ProjectTableRunner Initialized .....");
        if (multiTenantProperties.getEnableProject()) {
            // 查询 所有的 不包含 project_id 的表
            Set<String> tableNames = projectMapper.selectListTableNamesByNotIncludeColumn(PROJECT_ID);
            // 从 CmsProject 实体类中。获取 CmsProject 在 mysql 中的表名称。
            String projectTableName = CmsProject.class.getAnnotation(TableName.class).value();
            // 删除 CmsProject 的表名称
            tableNames.add(projectTableName);
            MbpConst.PROJECT_IGNORE_TABLES.addAll(tableNames);
            log.info("ignore project_id intercept table name : \n{}", JsonUtils.toJson(MbpConst.PROJECT_IGNORE_TABLES));
        }
    }

}
