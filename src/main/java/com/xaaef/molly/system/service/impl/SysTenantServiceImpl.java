package com.xaaef.molly.system.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.core.auth.enums.AdminFlag;
import com.xaaef.molly.core.auth.enums.GenderType;
import com.xaaef.molly.core.auth.enums.StatusEnum;
import com.xaaef.molly.core.tenant.DatabaseManager;
import com.xaaef.molly.core.tenant.base.service.impl.BaseServiceImpl;
import com.xaaef.molly.core.tenant.service.MultiTenantManager;
import com.xaaef.molly.core.tenant.util.TenantUtils;
import com.xaaef.molly.perms.entity.PmsDept;
import com.xaaef.molly.perms.entity.PmsRole;
import com.xaaef.molly.perms.entity.PmsUser;
import com.xaaef.molly.perms.mapper.PmsDeptMapper;
import com.xaaef.molly.perms.mapper.PmsRoleMapper;
import com.xaaef.molly.perms.mapper.PmsUserMapper;
import com.xaaef.molly.system.entity.SysTemplate;
import com.xaaef.molly.system.entity.SysTenant;
import com.xaaef.molly.system.mapper.SysTenantMapper;
import com.xaaef.molly.system.po.CreateTenantPO;
import com.xaaef.molly.system.po.TenantCreatedSuccessVO;
import com.xaaef.molly.system.service.SysConfigService;
import com.xaaef.molly.system.service.SysTemplateService;
import com.xaaef.molly.system.service.SysTenantService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
public class SysTenantServiceImpl extends BaseServiceImpl<SysTenantMapper, SysTenant> implements SysTenantService {

    private final MultiTenantManager tenantManager;

    private final DatabaseManager dataSourceManager;

    private final SysConfigService configService;

    private final SysTemplateService templateService;

    private final PmsUserMapper userMapper;

    private final PmsDeptMapper deptMapper;

    private final PmsRoleMapper roleMapper;


    /**
     * uuid
     */
    public static String getUUIDSuffix() {
        return IdUtil.fastUUID().split(StrUtil.DASHED)[4];
    }


    @Override
    public IPage<SysTenant> pageKeywords(SearchPO params) {
        IPage<SysTenant> result = super.pageKeywords(params,
                SysTenant::getName,
                SysTenant::getLinkman,
                SysTenant::getAddress
        );
        var collect = result.getRecords().stream().map(SysTenant::getTenantId).collect(Collectors.toSet());
        if (collect.isEmpty()) {
            return result;
        }
        var templateMap = templateService.listByTenantIds(collect);
        if (collect.isEmpty()) {
            return result;
        }
        result.getRecords().forEach(t -> {
            t.setTemplates(templateMap.get(t.getTenantId()));
        });
        return result;
    }


    @Override
    public IPage<SysTenant> simplePageKeywords(SearchPO params) {
        var wrapper = super.getKeywordsQueryWrapper2(
                params,
                SysTenant::getName,
                SysTenant::getLinkman,
                SysTenant::getAddress
        );
        wrapper.lambda().select(
                SysTenant::getTenantId,
                SysTenant::getLogo,
                SysTenant::getName,
                SysTenant::getLinkman
        );
        Page<SysTenant> pageRequest = Page.of(params.getPageIndex(), params.getPageSize());
        return super.page(pageRequest, wrapper);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(SysTenant entity) {
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
            var defaultLogoPath = Optional.ofNullable(configService.getValueByKey(TENANT_DEFAULT_LOGO))
                    .orElse("https://images.xaaef.com/molly_master_logo.png");
            entity.setLogo(defaultLogoPath);
        }
        updateTemplate(entity.getTenantId(), entity.getTemplateIds());
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
            po.setAdminUsername(getUUIDSuffix());
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
            var password = Optional.ofNullable(configService.getValueByKey(USER_DEFAULT_PASSWORD)).orElse("123456");
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

        // 新增权限模板
        if (po.getTemplateIds() != null && !po.getTemplateIds().isEmpty()) {
            var wrapper = new LambdaQueryWrapper<SysTemplate>()
                    .select(SysTemplate::getId)
                    .in(SysTemplate::getId, po.getTemplateIds());
            var templateIds = templateService.list(wrapper)
                    .stream()
                    .map(SysTemplate::getId)
                    .collect(Collectors.toSet());
            baseMapper.insertByTemplates(sysTenant.getTenantId(), templateIds);
        }

        // 新创建的 租户 创建表结构
        dataSourceManager.updateTable(sysTenant.getTenantId());

        // 将 新创建的 租户ID 保存到 redis 中
        tenantManager.addTenantId(sysTenant.getTenantId());

        // 租户默认角色名称
        String roleName = Optional.ofNullable(configService.getValueByKey(TENANT_DEFAULT_ROLE_NAME)).orElse("管理员");

        // 委托，新的租户id。执行初始化数据
        this.delegate(sysTenant.getTenantId(), () -> {

            var pmsDept = new PmsDept()
                    .setParentId(0L)
                    .setDeptName(po.getName())
                    .setLeader(po.getAdminNickname())
                    .setLeaderMobile(po.getAdminMobile())
                    .setSort(1L)
                    .setDescription(po.getName())
                    .setAncestors("0");

            // 保存部门
            deptMapper.insert(pmsDept);

            var pmsRole = new PmsRole()
                    .setRoleName(String.format("%s %s", po.getName(), roleName))
                    .setSort(1L);
            pmsRole.setDescription(pmsRole.getRoleName());

            // 保存角色
            roleMapper.insert(pmsRole);

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

            userMapper.insert(pmsUser);

            // 保存关联
            userMapper.insertByRoles(pmsUser.getUserId(), Set.of(pmsRole.getRoleId()));

            return true;
        });

        return TenantCreatedSuccessVO.builder()
                .adminMobile(po.getAdminMobile())
                .adminNickname(po.getAdminNickname())
                .adminUsername(po.getAdminUsername())
                .adminPassword(po.getAdminPwd())
                .build();
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateById(SysTenant entity) {
        if (!isMasterUser()) {
            throw new RuntimeException("只有系统用户，才能修改租户！");
        }
        updateTemplate(entity.getTenantId(), entity.getTemplateIds());
        return super.updateById(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateTemplate(String tenantId, Set<Long> templateIds) {
        if (!isMasterUser()) {
            throw new RuntimeException("只有系统用户，才能修改租户权限！");
        }
        // 先删除拥有的，模板ID
        baseMapper.deleteHaveTemplates(tenantId);
        if (templateIds == null || templateIds.isEmpty()) {
            return true;
        }
        // 租户新增，权限模板
        return baseMapper.insertByTemplates(tenantId, templateIds) > 0;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeById(Serializable id) {
        if (!isMasterUser() && !isAdminUser()) {
            throw new RuntimeException("非系统管理员，无法删除租户！");
        }
        var tenantId = String.valueOf(id);
        if (!this.exist(SysTenant::getTenantId, tenantId)) {
            return false;
        }
        if (StringUtils.equals(tenantId, tenantManager.getDefaultTenantId())) {
            throw new RuntimeException(StrUtil.format("无法删除 {} 默认租户！", tenantId));
        }
        // 删除 租户信息
        var flag = super.removeById(tenantId);
        // 删除 租户模板权限
        baseMapper.deleteHaveTemplates(tenantId);
        // 删除 租户数据库
        dataSourceManager.deleteTable(tenantId);
        // 删除 redis中租户
        tenantManager.removeTenantId(tenantId);
        return flag;
    }


}
