package com.xaaef.molly.system.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.xaaef.molly.core.auth.enums.AdminFlag;
import com.xaaef.molly.core.auth.enums.GenderType;
import com.xaaef.molly.core.auth.enums.StatusEnum;
import com.xaaef.molly.core.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.core.tenant.DatabaseManager;
import com.xaaef.molly.core.tenant.base.service.impl.BaseServiceImpl;
import com.xaaef.molly.core.tenant.service.MultiTenantManager;
import com.xaaef.molly.perms.entity.PmsDept;
import com.xaaef.molly.perms.entity.PmsRole;
import com.xaaef.molly.perms.entity.PmsUser;
import com.xaaef.molly.perms.repository.PmsDeptRepository;
import com.xaaef.molly.perms.repository.PmsRoleRepository;
import com.xaaef.molly.perms.repository.PmsUserRepository;
import com.xaaef.molly.system.entity.SysTenant;
import com.xaaef.molly.system.po.CreateTenantPO;
import com.xaaef.molly.system.po.TenantCreatedSuccessVO;
import com.xaaef.molly.system.repository.SysTenantRepository;
import com.xaaef.molly.system.service.CommConfigService;
import com.xaaef.molly.system.service.SysTenantService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.xaaef.molly.common.consts.ConfigName.*;
import static com.xaaef.molly.core.auth.jwt.JwtSecurityUtils.*;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/9 15:36
 */


@Slf4j
@Service
@AllArgsConstructor
public class SysTenantServiceImpl extends BaseServiceImpl<SysTenantRepository, SysTenant, String>
        implements SysTenantService {

    private final MultiTenantManager tenantManager;

    private final DatabaseManager dataSourceManager;

    private final CommConfigService configService;

    private final PmsUserRepository userReps;

    private final PmsDeptRepository deptReps;

    private final PmsRoleRepository roleReps;


    /**
     * uuid
     */
    public static String getUUIDSuffix() {
        return IdUtil.fastUUID().split(StrUtil.DASHED)[4];
    }


    @Override
    public SysTenant save(SysTenant entity) {
        if (!isMasterUser()) {
            throw new RuntimeException("只有系统用户，才能创建租户！");
        }
        if (StringUtils.isBlank(entity.getTenantId())) {
            do {
                entity.setTenantId(getUUIDSuffix());
            }
            while (tenantManager.existById(entity.getTenantId()));
        } else {
            if (tenantManager.existById(entity.getTenantId())) {
                throw new RuntimeException(String.format("租户ID %s 已经存在了！", entity.getTenantId()));
            }
        }
        if (entity.getExpired() == null) {
            entity.setExpired(LocalDateTime.now().plusYears(10));
        }
        if (entity.getStatus() == null) {
            entity.setStatus(StatusEnum.NORMAL.getCode());
        }
        if (entity.getLogo() == null) {
            var defaultLogoPath = configService.findValueByKey(TENANT_DEFAULT_LOGO);
            entity.setLogo(defaultLogoPath);
        }
        return super.save(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public TenantCreatedSuccessVO create(CreateTenantPO po) {
        if (!isMasterUser()) {
            throw new RuntimeException("只有系统用户，才能创建租户！");
        }
        // 如果 管理员 用户名为空，就随机给一个
        if (StringUtils.isBlank(po.getAdminUsername())) {
            // 随机生产一个 10 位的字符串。
            do {
                po.setAdminUsername(getUUIDSuffix());
            }
            while (userReps.existsByUsername(po.getAdminUsername()));
        } else {
            if (userReps.existsByUsername(po.getAdminUsername())) {
                throw new RuntimeException(String.format("管理员的用户名 %s 已经存在了！", po.getAdminUsername()));
            }
        }

        // 管理员邮箱,如果不填写，默认使用商户邮箱
        if (StringUtils.isBlank(po.getAdminEmail())) {
            po.setAdminEmail(po.getEmail());
        }

        // 管理员名称！如果不填写，默认是商户名称
        if (StringUtils.isBlank(po.getAdminNickname())) {
            po.setAdminNickname(po.getName());
        }

        // 管理员手机号,如果不填写，默认使用商户联系人手机号
        if (StringUtils.isBlank(po.getAdminMobile())) {
            po.setAdminMobile(po.getContactNumber());
        }

        // 默认密码
        if (StringUtils.isBlank(po.getAdminPwd())) {
            var password = configService.findValueByKey(USER_DEFAULT_PASSWORD);
            po.setAdminPwd(password);
        }

        var sysTenant = new SysTenant()
                .setTenantId(po.getTenantId())
                .setLogo(po.getLogo())
                .setName(po.getName())
                .setEmail(po.getEmail())
                .setLinkman(po.getLinkman())
                .setContactNumber(po.getContactNumber())
                .setAreaCode(po.getAreaCode())
                .setAddress(po.getAddress())
                .setExpired(po.getExpired());

        // 保存租户
        this.save(sysTenant);

        var pmsDept = new PmsDept()
                .setParentId(0L)
                .setDeptName(po.getName())
                .setLeader(po.getAdminNickname())
                .setLeaderMobile(po.getAdminMobile())
                .setSort(1L)
                .setDescription(po.getName())
                .setAncestors("0");
        super.saveFill(pmsDept);

        // 保存部门
        deptReps.saveAndFlush(pmsDept);

        // 租户默认角色名称
        String roleName = Objects.requireNonNull(configService.findValueByKey(TENANT_DEFAULT_ROLE_NAME), "管理员");

        var pmsRole = new PmsRole()
                .setRoleName(String.format("%s %s", po.getName(), roleName))
                .setSort(1L);
        pmsRole.setDescription(pmsRole.getRoleName());
        super.saveFill(pmsRole);
        // 保存角色
        roleReps.saveAndFlush(pmsRole);

        var password = encryptPassword(po.getAdminPwd());

        var pmsUser = new PmsUser()
                .setUserId(10001L)
                .setAvatar(sysTenant.getLogo())
                .setUsername(po.getAdminUsername())
                .setMobile(po.getAdminMobile())
                .setEmail(po.getAdminEmail())
                .setNickname(po.getAdminNickname())
                .setPassword(password)
                .setGender(GenderType.FEMALE.getCode())
                .setAdminFlag(AdminFlag.YES.getCode())
                .setStatus(StatusEnum.NORMAL.getCode())
                .setDeptId(pmsDept.getDeptId());
        super.saveFill(pmsUser);
        userReps.saveAndFlush(pmsUser);

        // 保存关联
        userReps.updateUserRoles(pmsUser.getUserId(), pmsRole.getRoleId());

        // 将 新创建的 租户ID 保存到 redis 中
        tenantManager.addTenantId(sysTenant.getTenantId());
        // 新创建的 租户 创建表结构
        dataSourceManager.createTable(sysTenant.getTenantId());

        return TenantCreatedSuccessVO.builder()
                .adminMobile(po.getAdminMobile())
                .adminNickname(po.getAdminNickname())
                .adminUsername(po.getAdminUsername())
                .adminPassword(po.getAdminPwd())
                .build();
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        if (isMasterUser() && isAdminUser()) {
            if (!existsById(id)) {
                return;
            }
            super.deleteById(id);
            tenantManager.removeTenantId(id);
            dataSourceManager.deleteTable(id);
        } else {
            throw new RuntimeException("非系统管理员，无法删除租户！");
        }
    }


}
