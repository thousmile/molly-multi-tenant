package com.xaaef.molly.core;

import com.xaaef.molly.core.auth.WebSecurityConfiguration;
import com.xaaef.molly.core.log.CustomLogConfig;
import com.xaaef.molly.core.redis.CustomRedisConfig;
import com.xaaef.molly.core.tenant.CustomMultiTenantConfig;
import com.xaaef.molly.core.web.CustomSpringWebConfig;
import com.xaaef.molly.core.xss.CustomXssFilterConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * <p>
 * 核心，自动 配置类
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/8/3 17:30
 */


@Slf4j
@Configuration
@AllArgsConstructor
@Import({
        CustomSpringWebConfig.class,
        CustomRedisConfig.class,
        CustomMultiTenantConfig.class,
        CustomLogConfig.class,
        CustomXssFilterConfig.class,
        WebSecurityConfiguration.class,
})
public class CoreAutoConfiguration {


}
