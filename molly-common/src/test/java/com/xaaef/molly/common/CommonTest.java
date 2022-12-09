package com.xaaef.molly.common;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.xaaef.molly.common.domain.RectPoint;
import com.xaaef.molly.common.util.RectRangeUtils;
import com.xaaef.molly.common.util.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

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
public class CommonTest {


    @Test
    public void test1() {
        var ua = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:108.0) Gecko/20100101 Firefox/108.0";
        var userAgent = ServletUtils.getUserAgent(ua);
        var browser = StrUtil.format("{} {}", userAgent.getBrowser(), userAgent.getVersion());
        System.out.println(browser);
        var os = StrUtil.format("{} {}", userAgent.getPlatform(), Objects.requireNonNullElse(userAgent.getOsVersion(), ""));
        System.out.println(os);
    }


    @Test
    public void test2() {
        // 需要检测的点位
        var points = List.of(
                new RectPoint<>(618.33D, 334.99D, 1L),
                new RectPoint<>(265D, 653D, 2L),
                new RectPoint<>(406.25D, 501.25D, 2L)
        );

        var topLeft = new RectPoint<Long>(289D, 209D);

        var bottomRight = new RectPoint<Long>(1029D, 549D);

        points.forEach(target -> {
            boolean a = RectRangeUtils.isIn(target, topLeft, bottomRight);
            System.out.printf("%s => %b\r\n", target.getId(), a);
        });
    }


    @Test
    public void test3() {
        int i = (19 / 100) + 1;
        System.out.println(i);
    }



    @Test
    public void test4() {
        var jwt = JWTUtil.parseToken("654wq1e45qw");
        var loginId = jwt.getPayloads().getStr(JWT.SUBJECT);
        System.out.println(loginId);
    }




}
