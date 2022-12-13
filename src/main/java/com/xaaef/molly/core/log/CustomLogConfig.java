package com.xaaef.molly.core.log;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/9 16:44
 */


@Slf4j
@Configuration
@Import({
        BindingResultAspect.class,
        OperateLogAspect.class,
})
@AllArgsConstructor
public class CustomLogConfig {



}
