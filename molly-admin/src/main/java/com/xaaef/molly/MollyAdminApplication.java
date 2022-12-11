package com.xaaef.molly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/9 10:49
 */


@EnableAsync
@SpringBootApplication
public class MollyAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(MollyAdminApplication.class, args);
    }

}
