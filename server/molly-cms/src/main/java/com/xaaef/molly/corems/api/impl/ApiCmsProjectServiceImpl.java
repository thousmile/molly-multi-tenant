package com.xaaef.molly.perms.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaaef.molly.common.enums.StatusEnum;
import com.xaaef.molly.corems.service.CmsProjectService;
import com.xaaef.molly.internal.api.ApiCmsProjectService;
import com.xaaef.molly.internal.dto.CmsProjectDTO;
import com.xaaef.molly.perms.entity.CmsProject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

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

    private final CmsProjectService projectService;


    @Override
    public CmsProjectDTO getSimpleById(Long projectId) {
        var wrapper = new LambdaQueryWrapper<CmsProject>()
                .select(List.of(CmsProject::getProjectId, CmsProject::getProjectName, CmsProject::getLeader))
                .eq(CmsProject::getStatus, StatusEnum.NORMAL.getCode())
                .eq(CmsProject::getProjectId, projectId);
        var source = projectService.getOne(wrapper);
        if (source == null) {
            return null;
        }
        return new CmsProjectDTO()
                .setProjectId(source.getProjectId())
                .setProjectName(source.getProjectName())
                .setLeader(source.getLeader());
    }


}
