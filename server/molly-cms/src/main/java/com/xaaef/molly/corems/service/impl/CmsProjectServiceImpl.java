package com.xaaef.molly.corems.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.corems.entity.CmsProject;
import com.xaaef.molly.corems.mapper.CmsProjectMapper;
import com.xaaef.molly.corems.po.ProjectQueryPO;
import com.xaaef.molly.corems.service.CmsProjectService;
import com.xaaef.molly.corems.vo.ResetPasswordVO;
import com.xaaef.molly.internal.api.ApiPmsDeptService;
import com.xaaef.molly.internal.dto.PmsDeptDTO;
import com.xaaef.molly.tenant.base.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xaaef.molly.auth.jwt.JwtSecurityUtils.*;


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
public class CmsProjectServiceImpl extends BaseServiceImpl<CmsProjectMapper, CmsProject> implements CmsProjectService {

    private final ApiPmsDeptService apiPmsDeptService;


    @Override
    public IPage<CmsProject> pageKeywords(ProjectQueryPO params) {
        var wrapper = super.getKeywordsQueryWrapper(params,
                List.of(CmsProject::getProjectName, CmsProject::getLinkman));
        if (params.getDeptId() != null && params.getDeptId() > 0L) {
            var childIds = apiPmsDeptService.listChildIdByDeptId(params.getDeptId());
            wrapper.lambda().in(CmsProject::getDeptId, childIds);
        }
        Page<CmsProject> pageRequest = Page.of(params.getPageIndex(), params.getPageSize());
        Page<CmsProject> result = super.page(pageRequest, wrapper);
        if (params.isIncludeCauu()) {
            reflectionFill(result.getRecords());
        }
        if (params.isIncludeDept()) {
            setDept(result.getRecords());
        }
        return result;
    }


    private void setDept(Collection<CmsProject> list) {
        if (!list.isEmpty()) {
            var deptIds = list.stream().map(CmsProject::getDeptId).collect(Collectors.toSet());
            if (deptIds.isEmpty()) {
                return;
            }
            var mapDept = apiPmsDeptService.mapByDeptIds(deptIds);
            list.forEach(p -> {
                var dept = mapDept.getOrDefault(p.getDeptId(), null);
                p.setPassword(null);
                p.setDept(dept);
            });
        }
    }


    @Override
    public boolean save(CmsProject entity) {
        if (StrUtil.isNotEmpty(entity.getPassword())) {
            entity.setPassword(encryptPassword(entity.getPassword()));
        }
        return super.save(entity);
    }


    @Override
    public boolean updateById(CmsProject entity) {
        entity.setPassword(null);
        return super.updateById(entity);
    }


    @Override
    public boolean resetPassword(ResetPasswordVO pwd) {
        var project = baseMapper.selectById(pwd.getProjectId());
        if (project != null) {
            // 新密码 加密
            String newPassword = encryptPassword(pwd.getNewPwd());
            // 修改
            var pmsUser = new CmsProject()
                    .setProjectId(pwd.getProjectId())
                    .setPassword(newPassword);
            return super.updateById(pmsUser);
        }
        throw new RuntimeException("项目不存在，无法重置密码！");
    }


    @Override
    public IPage<CmsProject> simplePageKeywords(SearchPO po) {
        var wrapper = super.getKeywordsQueryWrapper(po,
                        List.of(CmsProject::getProjectName, CmsProject::getLinkman)
                )
                .lambda()
                .select(
                        List.of(
                                CmsProject::getProjectId, CmsProject::getProjectName,
                                CmsProject::getLinkman, CmsProject::getAreaCode, CmsProject::getAddress
                        )
                )
                .eq(CmsProject::getStatus, 1)
                .orderByAsc(CmsProject::getCreateTime);
        // 非系统用户和管理员用户。根据所在部门，查询项目列表
        if (!isMasterUser() && !isAdminUser()) {
            var childDeptIds = apiPmsDeptService.listChildIdByDeptId(getLoginUser().getDeptId());
            if (!childDeptIds.isEmpty()) {
                wrapper.in(CmsProject::getDeptId, childDeptIds);
            }
        }
        Page<CmsProject> pageRequest = Page.of(po.getPageIndex(), po.getPageSize());
        return super.page(pageRequest, wrapper);
    }


    @Override
    public boolean removeById(CmsProject entity) {
        if (entity.getProjectId() == null) {
            throw new RuntimeException("请输入项目Id！");
        }
        if (StrUtil.isEmpty(entity.getPassword())) {
            throw new RuntimeException("请输入项目密码！");
        }
        var dbProject = super.getById(entity.getProjectId());
        if (dbProject == null) {
            throw new RuntimeException(StrUtil.format("项目Id {} 不存在！", entity.getProjectId()));
        }
        var flag = matchesPassword(entity.getPassword(), dbProject.getPassword());
        if (!flag) {
            throw new RuntimeException(StrUtil.format("项目 {} 密码输入错误！", dbProject.getProjectName()));
        }
        return super.removeById(dbProject.getProjectId());
    }


}
