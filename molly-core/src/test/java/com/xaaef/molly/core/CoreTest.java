package com.xaaef.molly.core;

import com.xaaef.molly.core.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.core.tenant.enums.DbStyle;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * <p>
 * 测试类
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/9 10:26
 */

@Slf4j
public class CoreTest {


    @Test
    public void test1() {
        String admin = JwtSecurityUtils.encryptPassword("admin");
        System.out.println(admin);
    }


    @Test
    public void test2() {
    }


}
