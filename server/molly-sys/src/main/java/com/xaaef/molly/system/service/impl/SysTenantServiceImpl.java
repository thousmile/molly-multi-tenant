package com.xaaef.molly.system.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.enums.StatusEnum;
import com.xaaef.molly.internal.api.ApiPmsUserService;
import com.xaaef.molly.internal.api.ApiSysConfigService;
import com.xaaef.molly.internal.dto.InitUserDTO;
import com.xaaef.molly.tenant.DatabaseManager;
import com.xaaef.molly.tenant.base.service.impl.BaseServiceImpl;
import com.xaaef.molly.tenant.service.MultiTenantManager;
import com.xaaef.molly.system.entity.SysTemplate;
import com.xaaef.molly.system.entity.SysTenant;
import com.xaaef.molly.system.mapper.SysTenantMapper;
import com.xaaef.molly.system.po.CreateTenantPO;
import com.xaaef.molly.system.po.TenantCreatedSuccessVO;
import com.xaaef.molly.system.service.SysTemplateService;
import com.xaaef.molly.system.service.SysTenantService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.xaaef.molly.auth.jwt.JwtSecurityUtils.*;
import static com.xaaef.molly.common.consts.ConfigName.*;

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
            throw new RuntimeException("??????????????????????????????????????????");
        }
        if (StringUtils.isBlank(entity.getTenantId())) {
            do {
                entity.setTenantId(getUUIDSuffix());
            }
            while (tenantManager.existById(entity.getTenantId()));
        } else {
            var isMatch = Pattern.matches("\\w{4,12}$", entity.getTenantId());
            if (!isMatch) {
                throw new RuntimeException("??????ID ?????????????????????????????????4~12???!???");
            }
            if (tenantManager.existById(entity.getTenantId())) {
                throw new RuntimeException(String.format("??????ID %s ??????????????????", entity.getTenantId()));
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
            throw new RuntimeException("??????????????????????????????????????????");
        }

        // ?????? ????????? ????????????????????????????????????
        if (StringUtils.isBlank(po.getAdminUsername())) {
            po.setAdminUsername(getUUIDSuffix());
        }

        // ???????????????,??????????????????????????????????????????
        if (StringUtils.isBlank(po.getAdminEmail())) {
            po.setAdminEmail(po.getEmail());
        }

        // ?????????????????????????????????????????????????????????
        if (StringUtils.isBlank(po.getAdminNickname())) {
            po.setAdminNickname(po.getName());
        }

        // ??????????????????,??????????????????????????????????????????????????????
        if (StringUtils.isBlank(po.getAdminMobile())) {
            po.setAdminMobile(po.getContactNumber());
        }

        // ????????????
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

        // ????????????
        this.save(sysTenant);

        // ??????????????????
        if (po.getTemplates() != null && !po.getTemplates().isEmpty()) {
            var tempIds = po.getTemplates().stream()
                    .map(SysTemplate::getId)
                    .collect(Collectors.toSet());
            var wrapper = new LambdaQueryWrapper<SysTemplate>()
                    .select(SysTemplate::getId)
                    .in(SysTemplate::getId, tempIds);
            var templateIds = templateService.list(wrapper)
                    .stream()
                    .map(SysTemplate::getId)
                    .collect(Collectors.toSet());
            baseMapper.insertByTemplates(sysTenant.getTenantId(), templateIds);
        }

        // ???????????? ?????? ???????????????
        dataSourceManager.updateTable(sysTenant.getTenantId());

        // ??? ???????????? ??????ID ????????? redis ???
        tenantManager.addTenantId(sysTenant.getTenantId());

        // ????????? ?????? ?????? ??????
        var initUserDTO = new InitUserDTO();
        BeanUtils.copyProperties(po, initUserDTO);
        userService.initUserAndRoleAndDept(initUserDTO);

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
            throw new RuntimeException("??????????????????????????????????????????");
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
            throw new RuntimeException("????????????????????????????????????????????????");
        }
        // ???????????????????????????ID
        baseMapper.deleteHaveTemplates(tenantId);
        if (templateIds == null || templateIds.isEmpty()) {
            return true;
        }
        // ???????????????????????????
        return baseMapper.insertByTemplates(tenantId, templateIds) > 0;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeById(Serializable id) {
        if (!isMasterUser() && !isAdminUser()) {
            throw new RuntimeException("??????????????????????????????????????????");
        }
        var tenantId = String.valueOf(id);
        if (!this.exist(SysTenant::getTenantId, tenantId)) {
            return false;
        }
        if (StringUtils.equals(tenantId, tenantManager.getDefaultTenantId())) {
            throw new RuntimeException(StrUtil.format("???????????? {} ???????????????", tenantId));
        }
        // ?????? ????????????
        var flag = super.removeById(tenantId);
        // ?????? ??????????????????
        baseMapper.deleteHaveTemplates(tenantId);
        // ?????? ???????????????
        dataSourceManager.deleteTable(tenantId);
        // ?????? redis?????????
        tenantManager.removeTenantId(tenantId);
        return flag;
    }


}
