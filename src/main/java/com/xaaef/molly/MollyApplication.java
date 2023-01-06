package com.xaaef.molly;

import cn.xuyanwu.spring.file.storage.EnableFileStorage;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>
 * spring boot jpa 多租户 启动类
 * </p>
 *
 * @author WangChenChen
 * @version 1.0.1
 * @date 2021/10/14 14:24
 */


@EnableAsync
@EnableCaching
@EnableScheduling
@EnableFileStorage
@SpringBootApplication
@MapperScan("com.xaaef.molly.*.mapper")
public class MollyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MollyApplication.class, args);
    }

}
