package com.xaaef.molly.corems.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.corems.entity.CmsProject;
import com.xaaef.molly.corems.mapper.CmsProjectMapper;
import com.xaaef.molly.corems.service.CmsProjectService;
import com.xaaef.molly.corems.vo.ResetPasswordVO;
import com.xaaef.molly.internal.api.ApiPmsDeptService;
import com.xaaef.molly.tenant.base.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
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
    public IPage<CmsProject> pageKeywords(SearchPO params, Collection<SFunction<CmsProject, ?>> columns) {
        var result = super.pageKeywords(params, columns);
        if (result.getTotal() > 0L) {
            var deptIds = result.getRecords().stream().map(CmsProject::getDeptId).collect(Collectors.toSet());
            if (!deptIds.isEmpty()) {
                var mapDept = apiPmsDeptService.mapByDeptIds(deptIds);
                result.getRecords().forEach(p -> {
                    var dept = mapDept.getOrDefault(p.getDeptId(), null);
                    p.setPassword(null);
                    p.setDept(dept);
                });
            }
        }
        return result;
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
        // 非管理员用户。根据所在部门，查询项目列表
        if (!isMasterUser()) {
            var childDeptIds = apiPmsDeptService.listChildIdByDeptId(getLoginUser().getDeptId());
            if (!childDeptIds.isEmpty()) {
                wrapper.in(CmsProject::getDeptId, childDeptIds);
            }
        }
        Page<CmsProject> pageRequest = Page.of(po.getPageIndex(), po.getPageSize());
        return super.page(pageRequest, wrapper);
    }


}
