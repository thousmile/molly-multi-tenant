package com.xaaef.molly.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.xaaef.molly.tenant.props.MultiTenantProperties;
import com.xaaef.molly.common.util.TenantUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.xaaef.molly.common.consts.MbpConst.*;


/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/14 12:40
 */


@Slf4j
@Component
@AllArgsConstructor
public class ProjectLineHandler implements TenantLineHandler {

    private final MultiTenantProperties multiTenantProperties;


    @Override
    public Expression getTenantId() {
        var projectId = Optional.ofNullable(TenantUtils.getProjectId())
                .orElse(multiTenantProperties.getDefaultProjectId());
        return new LongValue(projectId);
    }


    /**
     * 获取 项目 的字段名
     */
    @Override
    public String getTenantIdColumn() {
        return PROJECT_ID;
    }


    /**
     * 过滤不需要拼接 项目ID 的表
     * 默认返回 false 表示所有表都需要拼接条件
     */
    @Override
    public boolean ignoreTable(String tableName) {
        if (PROJECT_IGNORE_TABLES.isEmpty()) {
            return false;
        }
        return PROJECT_IGNORE_TABLES.stream().anyMatch(t -> t.equalsIgnoreCase(tableName));
    }


}
