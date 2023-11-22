package com.xaaef.molly.corems.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaaef.molly.common.consts.DefConfigValue;
import com.xaaef.molly.common.enums.StatusEnum;
import com.xaaef.molly.corems.entity.CmsProject;
import com.xaaef.molly.corems.entity.TenantAndProject;
import com.xaaef.molly.corems.mapper.CmsProjectMapper;
import com.xaaef.molly.internal.api.ApiCmsProjectService;
import com.xaaef.molly.internal.api.ApiPmsDeptService;
import com.xaaef.molly.internal.api.ApiSysConfigService;
import com.xaaef.molly.internal.dto.CmsProjectDTO;
import com.xaaef.molly.internal.dto.SysTenantDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.xaaef.molly.auth.jwt.JwtSecurityUtils.encryptPassword;
import static com.xaaef.molly.common.consts.ConfigName.PROJECT_DEFAULT_PASSWORD;
import static com.xaaef.molly.tenant.util.DelegateUtils.delegate;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/2/14 11:48
 */


@Slf4j
@Service
public class ApiCmsProjectServiceImpl implements ApiCmsProjectService {

    @Autowired
    @Lazy
    private ApiPmsDeptService pmsDeptService;

    private final CmsProjectMapper projectMapper;

    private final ApiSysConfigService configService;


    public ApiCmsProjectServiceImpl(CmsProjectMapper projectMapper,
                                    ApiSysConfigService configService) {
        this.projectMapper = projectMapper;
        this.configService = configService;
    }


    @Override
    public CmsProjectDTO getSimpleById(Long projectId) {
        var wrapper = new LambdaQueryWrapper<CmsProject>()
                .select(
                        List.of(
                                CmsProject::getProjectId, CmsProject::getProjectName,
                                CmsProject::getLinkman, CmsProject::getAreaCode, CmsProject::getAddress
                        )
                )
                .eq(CmsProject::getStatus, StatusEnum.NORMAL.getCode())
                .eq(CmsProject::getProjectId, projectId);
        var source = projectMapper.selectOne(wrapper);
        if (source == null) {
            return null;
        }
        var target = new CmsProjectDTO();
        BeanUtils.copyProperties(source, target);
        return target;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void initProject(SysTenantDTO po) {
        var password = Optional.ofNullable(configService.getValueByKey(PROJECT_DEFAULT_PASSWORD))
                .orElse("123456");
        // 委托，新的租户id。执行初始化数据
        delegate(po.getTenantId(), () -> {
            var project = new CmsProject()
                    .setProjectId(DefConfigValue.DEFAULT_PROJECT_ID)
                    .setProjectName("默认项目")
                    .setLinkman(po.getLinkman())
                    .setContactNumber(po.getContactNumber())
                    .setAreaCode(po.getAreaCode())
                    .setAddress(po.getAddress())
                    .setSort(1L)
                    .setPassword(encryptPassword(password))
                    .setStatus(StatusEnum.NORMAL.getCode())
                    .setDeptId(DefConfigValue.DEFAULT_DEPT_ID);
            projectMapper.insert(project);
            var result = new CmsProjectDTO();
            BeanUtils.copyProperties(project, result);
            return result;
        });
    }


    @Override
    public long countProjectByDeptId(Long deptId) {
        var wrapper = new LambdaQueryWrapper<CmsProject>()
                .eq(CmsProject::getDeptId, deptId);
        return projectMapper.selectCount(wrapper);
    }


    @Override
    public Set<Long> listProjectByDeptId(Long deptId) {
        var deptIds = pmsDeptService.listChildIdByDeptId(deptId);
        var wrapper = new LambdaQueryWrapper<CmsProject>()
                .select(CmsProject::getProjectId)
                .in(CmsProject::getDeptId, deptIds);
        return projectMapper.selectList(wrapper)
                .stream()
                .map(CmsProject::getProjectId)
                .collect(Collectors.toSet());
    }


    @Override
    public Map<String, Set<Long>> mapByTenantDbName(String dbNamePrefix, Collection<String> tenantIds) {
        if (tenantIds.isEmpty()) {
            return new HashMap<>();
        }
        var tenantDbNameList = tenantIds.stream()
                .map(tenantId -> dbNamePrefix + tenantId)
                .collect(Collectors.toSet());
        return projectMapper.selectListByTenantDbName(new HashSet<>(tenantDbNameList))
                .stream()
                .peek(t -> {
                    var newTenantId = t.getTenantId().replaceFirst(dbNamePrefix, "");
                    t.setTenantId(newTenantId);
                })
                .collect(
                        Collectors.groupingBy(
                                TenantAndProject::getTenantId,
                                Collectors.mapping(project -> project.getProjectId(), Collectors.toSet())
                        )
                );
    }


}
