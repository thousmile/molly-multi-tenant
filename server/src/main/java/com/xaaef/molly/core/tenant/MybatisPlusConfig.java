package com.xaaef.molly.core.tenant;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.xaaef.molly.common.util.JsonUtils;
import com.xaaef.molly.core.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.core.tenant.props.MultiTenantProperties;
import com.xaaef.molly.core.tenant.schema.SchemaInterceptor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;

import static com.xaaef.molly.core.tenant.consts.MbpConst.*;


/**
 * <p>
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/8 9:21
 */


@Slf4j
@Configuration
@AllArgsConstructor
@EnableTransactionManagement
public class MybatisPlusConfig {

    private final MultiTenantProperties multiTenantProperties;

    /**
     * 单页分页条数限制(默认无限制,参见 插件#handlerLimit 方法)
     */
    private static final Long MAX_LIMIT = 100L;

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,
     * 需要设置 MybatisConfiguration#useDeprecatedExecutor = false
     * 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        // 设置 ObjectMapper
        JacksonTypeHandler.setObjectMapper(JsonUtils.getMapper());

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 是否启用租户
        if (multiTenantProperties.getEnable()) {
            var schemaInterceptor = new SchemaInterceptor(multiTenantProperties);
            interceptor.addInnerInterceptor(schemaInterceptor);
        }

        // 是否启用项目
        if (multiTenantProperties.getEnableProject()) {
            interceptor.addInnerInterceptor(
                    new TenantLineInnerInterceptor(
                            new ProjectLineHandler(multiTenantProperties)
                    )
            );
        }

        //分页插件: PaginationInnerInterceptor
        var paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setMaxLimit(MAX_LIMIT);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);

        //防止全表更新与删除插件: BlockAttackInnerInterceptor
        var blockAttackInnerInterceptor = new BlockAttackInnerInterceptor();
        interceptor.addInnerInterceptor(blockAttackInnerInterceptor);

        return interceptor;
    }


    @Slf4j
    @Component
    public static class MyMetaObjectHandler implements MetaObjectHandler {

        @Override
        public void insertFill(MetaObject metaObject) {
            if (JwtSecurityUtils.isAuthenticated()) {
                var userId = JwtSecurityUtils.getUserId();
                this.strictInsertFill(metaObject, ATTR_CREATE_USER, () -> userId, Long.class);
                this.strictInsertFill(metaObject, ATTR_LAST_UPDATE_USER, () -> userId, Long.class);
            }
            this.strictInsertFill(metaObject, ATTR_CREATE_TIME, LocalDateTime::now, LocalDateTime.class);
            this.strictInsertFill(metaObject, ATTR_LAST_UPDATE_TIME, LocalDateTime::now, LocalDateTime.class);
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            if (JwtSecurityUtils.isAuthenticated()) {
                var userId = JwtSecurityUtils.getUserId();
                this.strictUpdateFill(metaObject, ATTR_LAST_UPDATE_USER, () -> userId, Long.class);
            }
            this.strictUpdateFill(metaObject, ATTR_LAST_UPDATE_TIME, LocalDateTime::now, LocalDateTime.class);
        }

    }

}
