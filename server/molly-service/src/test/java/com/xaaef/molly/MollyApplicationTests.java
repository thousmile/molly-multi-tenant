package com.xaaef.molly;

import cn.hutool.core.util.RandomUtil;
import com.xaaef.molly.auth.enums.GrantType;
import com.xaaef.molly.auth.jwt.JwtLoginUser;
import com.xaaef.molly.common.consts.ConfigNameConst;
import com.xaaef.molly.common.enums.AdminFlag;
import com.xaaef.molly.common.enums.GenderType;
import com.xaaef.molly.common.enums.StatusEnum;
import com.xaaef.molly.common.enums.UserType;
import com.xaaef.molly.common.util.JsonUtils;
import com.xaaef.molly.common.util.TenantUtils;
import com.xaaef.molly.corems.service.CmsProjectService;
import com.xaaef.molly.system.entity.SysTemplate;
import com.xaaef.molly.system.po.CreateTenantPO;
import com.xaaef.molly.system.service.SysConfigService;
import com.xaaef.molly.system.service.SysTenantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.xaaef.molly.system.cron.TestCronAsync.randomChinese;


@SpringBootTest
public class MollyApplicationTests {

    @Autowired
    private SysTenantService tenantService;

    @Autowired
    private SysConfigService configService;

    @Autowired
    private CmsProjectService projectService;

    static {
        var userDetails = new JwtLoginUser()
                .setAdminFlag(AdminFlag.YES)
                .setUserType(UserType.SYSTEM)
                .setUserId(19980817L)
                .setTenantId("master")
                .setGrantType(GrantType.PASSWORD)
                .setGender(GenderType.MALE.getCode())
                .setDeptId(10001L)
                .setStatus(StatusEnum.NORMAL);
        var authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        // 将用户信息，设置到 SecurityContext 中，可以在任何地方 使用 下面语句获取 获取 当前用户登录信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        TenantUtils.setProjectId(10001L);
        TenantUtils.setTenantId("master");
    }


    /**
     * 随机生成租户
     *
     * @author WangChenChen
     * @version 2.0
     * @date 2023/10/26 9:24
     */
    @Test
    public void test1() throws Exception {
        var areaCodeList = List.of(440301000000L, 440303000000L, 440304000000L, 440305000000L,
                440306000000L, 440307000000L, 440308000000L, 440309000000L, 440310000000L, 440311000000L);
        for (int i = 0; i < 3; i++) {
            var start = System.currentTimeMillis();
            var email = String.format("%s@qq.com", RandomUtil.randomString(10));
            var contactNumber = String.format("0755-%s", RandomUtil.randomNumbers(7));
            var expired = LocalDateTime.now().plusDays(RandomUtil.randomInt(30, 3650));
            var ac = areaCodeList.get(RandomUtil.randomInt(0, (areaCodeList.size() - 1)));
            var po = new CreateTenantPO()
                    .setName(randomChinese(5))
                    .setEmail(email)
                    .setLinkman(randomChinese(3))
                    .setContactNumber(contactNumber)
                    .setLogo("https://images.xaaef.com/molly_master_logo.png")
                    .setAreaCode(ac)
                    .setAddress(randomChinese(10))
                    .setTemplates(Set.of(new SysTemplate().setId(10001L)))
                    .setExpired(expired);
            var success = tenantService.create(po);
            System.out.println(JsonUtils.toFormatJson(success));
            var end = System.currentTimeMillis() - start;
            System.out.printf("耗时: %d ms\n", end);
            // 异步初始化，需要等创建数据库表结构完成
            Thread.sleep(Duration.ofSeconds(20));
        }
    }


    /**
     * 获取项目
     *
     * @author WangChenChen
     * @version 2.0
     * @date 2023/10/26 9:24
     */
    @Test
    public void test2() throws Exception {
        TenantUtils.setTenantId("master");
        var valueByKey = configService.getValueByKey(ConfigNameConst.USER_DEFAULT_PASSWORD.getKey());
        System.out.println(valueByKey);
    }


    /**
     * 获取项目
     *
     * @author WangChenChen
     * @version 2.0
     * @date 2023/10/26 9:24
     */
    @Test
    public void test3() throws Exception {
        TenantUtils.setTenantId("master");
        projectService.list()
                .forEach(System.out::println);
        TenantUtils.setTenantId("google");
        projectService.list()
                .forEach(System.out::println);
    }


}
