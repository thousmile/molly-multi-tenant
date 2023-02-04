package com.xaaef.molly;

import cn.hutool.core.io.FileUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


class NoSpringTests {

    @Test
    public void test1() {
        boolean after = LocalDateTime.now().isAfter(LocalDateTime.of(LocalDate.of(2023, 12, 28), LocalTime.MAX));
        System.out.println(after);
    }


    @Test
    public void test3() {
        System.out.println(FileUtil.extName("https://images.xaaef.com/base/63b7df8b9e8581f5bff107e2.jpg"));
    }

}
