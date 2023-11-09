package com.xaaef.molly.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 * 线程池
 * </p>
 *
 * @author WangChenChen
 * @version 1.0.1
 * @date 2023/11/09 16:52
 */

@Slf4j
@Configuration
public class TaskExecutorConfiguration {

    @Bean
    @Primary
    public ThreadPoolTaskExecutor taskExecutor() {
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setKeepAliveSeconds(60);
        executor.setQueueCapacity(10000);
        executor.setThreadNamePrefix("task-");
        // 线程不够用时由调用的线程处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }


}
