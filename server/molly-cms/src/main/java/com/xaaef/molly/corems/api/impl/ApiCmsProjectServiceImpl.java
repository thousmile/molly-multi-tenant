package com.xaaef.molly.corems.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaaef.molly.common.enums.StatusEnum;
import com.xaaef.molly.corems.mapper.CmsProjectMapper;
import com.xaaef.molly.internal.api.ApiCmsProjectService;
import com.xaaef.molly.internal.api.ApiSysConfigService;
import com.xaaef.molly.internal.dto.CmsProjectDTO;
import com.xaaef.molly.internal.dto.SysTenantDTO;
import com.xaaef.molly.perms.entity.CmsProject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.xaaef.molly.auth.jwt.JwtSecurityUtils.encryptPassword;
import static com.xaaef.molly.common.consts.ConfigName.USER_DEFAULT_PASSWORD;
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
@AllArgsConstructor
public class ApiCmsProjectServiceImpl implements ApiCmsProjectService {

    private final CmsProjectMapper projectMapper;

    private final ApiSysConfigService configService;


    @Override
    public CmsProjectDTO getSimpleById(Long projectId) {
        var wrapper = new LambdaQueryWrapper<CmsProject>()
                .select(List.of(CmsProject::getProjectId, CmsProject::getProjectName,
                        CmsProject::getLinkman, CmsProject::getContactNumber))
                .eq(CmsProject::getStatus, StatusEnum.NORMAL.getCode())
                .eq(CmsProject::getProjectId, projectId);
        var source = projectMapper.selectOne(wrapper);
        if (source == null) {
            return null;
        }
        return new CmsProjectDTO()
                .setProjectId(source.getProjectId())
                .setProjectName(source.getProjectName())
                .setLinkman(source.getLinkman())
                .setContactNumber(source.getContactNumber());
    }


    @Override
    public void initProject(SysTenantDTO po) {
        var password = Optional.ofNullable(configService.getValueByKey(USER_DEFAULT_PASSWORD))
                .orElse("123456");
        // 委托，新的租户id。执行初始化数据
        delegate(po.getTenantId(), () -> {
            var project = new CmsProject()
                    .setProjectId(10001L)
                    .setProjectName(po.getName())
                    .setLinkman(po.getLinkman())
                    .setContactNumber(po.getContactNumber())
                    .setAreaCode(po.getAreaCode())
                    .setAddress(po.getAddress())
                    .setSort(1L)
                    .setPassword(encryptPassword(password))
                    .setStatus((byte) 1)
                    .setDeptId(10001L);
            return projectMapper.insert(project);
        });
    }


    @Override
    public long countProjectByDeptId(Long deptId) {
        var wrapper = new LambdaQueryWrapper<CmsProject>()
                .eq(CmsProject::getDeptId, deptId);
        return projectMapper.selectCount(wrapper);
    }


}
