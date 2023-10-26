package com.xaaef.molly;

import cn.hutool.core.util.RandomUtil;
import com.xaaef.molly.auth.enums.GrantType;
import com.xaaef.molly.auth.jwt.JwtLoginUser;
import com.xaaef.molly.common.enums.AdminFlag;
import com.xaaef.molly.common.enums.GenderType;
import com.xaaef.molly.common.enums.StatusEnum;
import com.xaaef.molly.common.enums.UserType;
import com.xaaef.molly.common.util.JsonUtils;
import com.xaaef.molly.common.util.TenantUtils;
import com.xaaef.molly.system.entity.SysTemplate;
import com.xaaef.molly.system.po.CreateTenantPO;
import com.xaaef.molly.system.service.SysTenantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Set;


@SpringBootTest
public class MollyApplicationTests {

    @Autowired
    private SysTenantService tenantService;

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
    public void test1() {
        for (int i = 0; i < 5; i++) {
            var email = String.format("%s@qq.com", RandomUtil.randomString(10));
            var contactNumber = String.format("0755-%s", RandomUtil.randomNumbers(7));
            var expired = LocalDateTime.now().plusDays(RandomUtil.randomInt(30, 3650));
            var po = new CreateTenantPO()
                    .setName(randomChinese(5))
                    .setEmail(email)
                    .setLinkman(randomChinese(3))
                    .setContactNumber(contactNumber)
                    .setLogo("https://images.xaaef.com/molly_master_logo.png")
                    .setAreaCode(440307000000L)
                    .setAddress(randomChinese(10))
                    .setTemplates(Set.of(new SysTemplate().setId(10001L)))
                    .setExpired(expired);
            var success = tenantService.create(po);
            System.out.println(JsonUtils.toFormatJson(success));
        }
    }


    private String randomChinese(int len) {
        var chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = RandomUtil.randomChinese();
        }
        return new String(chars);
    }

}
