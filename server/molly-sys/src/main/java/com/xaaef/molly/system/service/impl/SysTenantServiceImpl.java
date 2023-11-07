package com.xaaef.molly.system.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xaaef.molly.common.enums.StatusEnum;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.internal.api.ApiCmsProjectService;
import com.xaaef.molly.internal.api.ApiOperateUserService;
import com.xaaef.molly.internal.api.ApiPmsUserService;
import com.xaaef.molly.internal.api.ApiSysConfigService;
import com.xaaef.molly.internal.dto.InitUserDTO;
import com.xaaef.molly.internal.dto.SysTenantDTO;
import com.xaaef.molly.system.entity.SysTemplate;
import com.xaaef.molly.system.entity.SysTenant;
import com.xaaef.molly.system.mapper.SysTenantMapper;
import com.xaaef.molly.system.po.CreateTenantPO;
import com.xaaef.molly.system.po.TenantCreatedSuccessVO;
import com.xaaef.molly.system.service.SysTemplateService;
import com.xaaef.molly.system.service.SysTenantService;
import com.xaaef.molly.system.service.SysUserService;
import com.xaaef.molly.system.vo.TenantSimpleDataVO;
import com.xaaef.molly.tenant.DatabaseManager;
import com.xaaef.molly.tenant.base.service.impl.BaseServiceImpl;
import com.xaaef.molly.tenant.service.MultiTenantManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.xaaef.molly.auth.jwt.JwtSecurityUtils.*;
import static com.xaaef.molly.common.consts.ConfigName.TENANT_DEFAULT_LOGO;
import static com.xaaef.molly.common.consts.ConfigName.USER_DEFAULT_PASSWORD;
import static com.xaaef.molly.tenant.util.DelegateUtils.delegate;

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

    private final ApiSysConfigService configService;

    private final SysTemplateService templateService;

    private final ApiPmsUserService userService;

    private final SysUserService sysUserService;

    private final ApiCmsProjectService projectService;


    /**
     * uuid
     */
    public static String getUUIDSuffix() {
        return IdUtil.fastUUID().split(StrUtil.DASHED)[4];
    }


    @Override
    public IPage<SysTenant> pageKeywords(SearchPO params) {
        IPage<SysTenant> result = super.pageKeywords(
                params,
                List.of(SysTenant::getName, SysTenant::getLinkman, SysTenant::getAddress)
        );
        if (!result.getRecords().isEmpty()) {
            var collect = result.getRecords().stream()
                    .map(SysTenant::getTenantId)
                    .collect(Collectors.toSet());
            var templateMap = templateService.listByTenantIds(collect);
            result.getRecords().forEach(t -> {
                t.setTemplates(templateMap.getOrDefault(t.getTenantId(), new HashSet<>()));
            });
        }
        return result;
    }


    @Override
    public IPage<SysTenant> simplePageKeywords(SearchPO params) {
        var wrapper = super.getKeywordsQueryWrapper(
                params,
                List.of(SysTenant::getName, SysTenant::getLinkman, SysTenant::getAddress)
        );
        wrapper.lambda().select(
                List.of(SysTenant::getTenantId, SysTenant::getLogo, SysTenant::getName, SysTenant::getLinkman)
        );
        // 如果当前登录的用户，关联的有租户，
        var tenantIds = sysUserService.listHaveTenantIds(getUserId());
        if (!tenantIds.isEmpty()) {
            wrapper.lambda().in(SysTenant::getTenantId, tenantIds);
        }
        Page<SysTenant> pageRequest = Page.of(params.getPageIndex(), params.getPageSize());
        return super.page(pageRequest, wrapper);
    }


    @Override
    public Collection<TenantSimpleDataVO> simpleSearchQuery(SearchPO po) {
        if (StringUtils.isBlank(po.getKeywords())) {
            return new ArrayList<>();
        }
        return delegate(tenantManager.getDefaultTenantId(), () -> {
            var wrapper = new LambdaQueryWrapper<SysTenant>()
                    .select(List.of(SysTenant::getTenantId, SysTenant::getLogo, SysTenant::getName))
                    .like(SysTenant::getTenantId, po.getKeywords())
                    .or()
                    .like(SysTenant::getName, po.getKeywords());
            Page<SysTenant> pageRequest = Page.of(po.getPageIndex(), po.getPageSize());
            return super.page(pageRequest, wrapper)
                    .getRecords()
                    .stream()
                    .map(t -> new TenantSimpleDataVO()
                            .setTenantId(t.getTenantId())
                            .setLogo(t.getLogo())
                            .setName(t.getName())
                    ).collect(Collectors.toSet());
        });
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
            var isMatch = Pattern.matches("\\w{4,12}$", entity.getTenantId());
            if (!isMatch) {
                throw new RuntimeException("租户ID 只能是字母和数字，长度4~12位!！");
            }
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
        if (entity.getTemplates() != null && !entity.getTemplates().isEmpty()) {
            var templateIds = entity.getTemplates()
                    .stream()
                    .map(SysTemplate::getId)
                    .collect(Collectors.toSet());
            updateTemplate(entity.getTenantId(), templateIds);
        }
        return super.save(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public TenantCreatedSuccessVO create(CreateTenantPO po) {
        if (!isMasterUser()) {
            throw new RuntimeException("只有系统用户，才能创建租户！");
        }

        // 如果 租户ID 为空，就随机给一个
        if (StringUtils.isBlank(po.getTenantId())) {
            po.setTenantId(getUUIDSuffix());
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
            var password = Optional.ofNullable(configService.getValueByKey(USER_DEFAULT_PASSWORD))
                    .orElse("123456");
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
        if (po.getTemplates() != null && !po.getTemplates().isEmpty()) {
            var tempIds = po.getTemplates().stream()
                    .map(SysTemplate::getId)
                    .collect(Collectors.toSet());
            var wrapper = new LambdaQueryWrapper<SysTemplate>()
                    .select(List.of(SysTemplate::getId))
                    .in(SysTemplate::getId, tempIds);
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

        // 初始化 用户 角色 部门
        var initUserDTO = new InitUserDTO();
        BeanUtils.copyProperties(po, initUserDTO);
        userService.initUserAndRoleAndDept(initUserDTO);

        // 初始化 项目
        var initTenantDTO = new SysTenantDTO();
        BeanUtils.copyProperties(sysTenant, initTenantDTO);
        projectService.initProject(initTenantDTO);

        return TenantCreatedSuccessVO.builder()
                .adminMobile(po.getAdminMobile())
                .adminNickname(po.getAdminNickname())
                .adminEmail(po.getAdminEmail())
                .adminUsername(po.getAdminUsername())
                .adminPwd(po.getAdminPwd())
                .build();
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateById(SysTenant entity) {
        if (!isMasterUser()) {
            throw new RuntimeException("只有系统用户，才能修改租户！");
        }
        var templateIds = entity.getTemplates()
                .stream()
                .map(SysTemplate::getId)
                .collect(Collectors.toSet());
        updateTemplate(entity.getTenantId(), templateIds);
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
