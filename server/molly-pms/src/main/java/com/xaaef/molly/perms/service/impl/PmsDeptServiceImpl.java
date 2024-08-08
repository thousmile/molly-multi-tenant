package com.xaaef.molly.perms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xaaef.molly.common.exception.BizException;
import com.xaaef.molly.common.po.SearchParentPO;
import com.xaaef.molly.internal.api.ApiCmsProjectService;
import com.xaaef.molly.perms.entity.PmsDept;
import com.xaaef.molly.perms.entity.PmsUser;
import com.xaaef.molly.perms.mapper.PmsDeptMapper;
import com.xaaef.molly.perms.mapper.PmsUserMapper;
import com.xaaef.molly.perms.service.PmsDeptService;
import com.xaaef.molly.tenant.base.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
public class PmsDeptServiceImpl extends BaseServiceImpl<PmsDeptMapper, PmsDept> implements PmsDeptService {

    private final PmsUserMapper userMapper;

    private final ApiCmsProjectService projectService;

    @Override
    public IPage<PmsDept> pageKeywords(SearchParentPO po) {
        var wrapper = super.getKeywordsQueryWrapper(
                po,
                List.of(
                        PmsDept::getDeptName, PmsDept::getDescription,
                        PmsDept::getLeader, PmsDept::getLeaderMobile
                )
        );
        if (po.getParentId() != null) {
            wrapper.lambda().eq(PmsDept::getParentId, po.getParentId());
        }
        Page<PmsDept> pageRequest = Page.of(po.getPageIndex(), po.getPageSize());
        return super.page(pageRequest, wrapper);
    }


    @Override
    public List<Tree<Long>> treeNode() {
        var nodeList = this.list().stream().map(r -> {
            var node = new TreeNode<>(r.getDeptId(), r.getParentId(), r.getDeptName(), r.getSort());
            var targetMap = new HashMap<String, Object>();
            BeanUtil.beanToMap(r, targetMap, CopyOptions.create().setIgnoreNullValue(true));
            node.setExtra(targetMap);
            return node;
        }).collect(Collectors.toList());
        return TreeUtil.build(nodeList, 0L);
    }


    @Override
    public Set<Long> listHaveDeptIds(Long userId) {
        var wrapper = new LambdaQueryWrapper<PmsUser>()
                .select(List.of(PmsUser::getDeptId)).eq(PmsUser::getUserId, userId);
        var one = userMapper.selectOne(wrapper);
        if (one == null) {
            return Set.of();
        }
        return listChildIdByDeptId(one.getDeptId());
    }


    @Override
    public Set<Long> listChildIdByDeptId(Long deptId) {
        var list = Optional.ofNullable(baseMapper.selectChildDeptId(deptId))
                .orElse(new HashSet<>());
        list.add(deptId);
        return list;
    }


    @Override
    public Set<PmsDept> listChildByDeptId(Long deptId) {
        var list = Optional.ofNullable(baseMapper.selectChildDept(deptId))
                .orElse(new HashSet<>());
        list.add(super.getById(deptId));
        return list;
    }


    @Override
    public Map<Long, String> mapDeptFullName(Set<Long> deptIds) {
        var w1 = new LambdaQueryWrapper<PmsDept>()
                .select(List.of(PmsDept::getDeptId, PmsDept::getDeptName, PmsDept::getAncestors))
                .in(PmsDept::getDeptId, deptIds);
        var deptList = super.list(w1);
        includeParentName(deptList);
        return deptList.stream()
                .collect(
                        Collectors.toMap(
                                PmsDept::getDeptId,
                                PmsDept::getDeptName,
                                (o1, o2) -> o2, HashMap::new
                        )
                );
    }


    @Override
    public List<PmsDept> listDeptFullName(Set<Long> deptIds) {
        var deptList = super.listByIds(deptIds);
        includeParentName(deptList);
        return deptList;
    }


    private void includeParentName(Collection<PmsDept> deptList) {
        if (CollectionUtil.isNotEmpty(deptList)) {
            // 获取全部 祖先ID
            var ancestors = deptList.stream()
                    .filter(a -> StrUtil.isNotBlank(a.getAncestors()))
                    .map(a -> a.getAncestors().split(StrUtil.COMMA))
                    .flatMap(Stream::of)
                    .filter(StrUtil::isNotBlank)
                    .map(Long::parseLong)
                    .filter(a -> a > 0)
                    .collect(Collectors.toSet());
            if (!ancestors.isEmpty()) {
                var w1 = new LambdaQueryWrapper<PmsDept>()
                        .select(List.of(PmsDept::getDeptId, PmsDept::getDeptName))
                        .in(PmsDept::getDeptId, ancestors);
                var nameMaps = new HashMap<Long, String>();
                deptList.forEach(a -> nameMaps.put(a.getDeptId(), a.getDeptName()));
                baseMapper.selectList(w1).forEach(a -> nameMaps.put(a.getDeptId(), a.getDeptName()));
                if (!nameMaps.isEmpty()) {
                    deptList.forEach(d -> {
                        if (StrUtil.isNotBlank(d.getAncestors())) {
                            var parentIds = Arrays.stream(d.getAncestors().split(StrUtil.COMMA))
                                    .map(Long::parseLong)
                                    .filter(a -> a > 0)
                                    .collect(Collectors.toCollection(LinkedHashSet::new));
                            parentIds.add(d.getDeptId());
                            var fullName = parentIds.stream()
                                    .map(nameMaps::get)
                                    .filter(StrUtil::isNotBlank)
                                    .collect(Collectors.joining(StrUtil.SLASH));
                            d.setDeptName(fullName);
                        }
                    });
                }
            }
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(PmsDept entity) {
        if (entity.getParentId() != null && entity.getParentId() == 0L) {
            entity.setAncestors(String.valueOf(0L));
        } else {
            var parentDept = super.getById(entity.getParentId());
            if (parentDept == null) {
                throw new BizException(String.format("上级部门 %d 不存在!", entity.getParentId()));
            }
            entity.setAncestors(parentDept.getAncestors() + StrUtil.COMMA + entity.getParentId());
        }
        entity.setDeptId(null);
        return super.save(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeById(Serializable id) {
        var dept = getById(id);
        if (dept == null) {
            throw new BizException(String.format("部门 %s 不存在!", id));
        }
        if (super.exist(PmsDept::getParentId, dept.getDeptId())) {
            throw new BizException(String.format("请先删除 %s 的下级部门!", dept.getDeptName()));
        }
        if (projectService.countProjectByDeptId(dept.getDeptId()) > 0) {
            throw new BizException(String.format("部门 %s 还有项目关联!", dept.getDeptName()));
        }
        var wrapper = new LambdaQueryWrapper<PmsUser>()
                .eq(PmsUser::getDeptId, dept.getDeptId());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BizException(String.format("部门 %s 还有用户关联!", dept.getDeptName()));
        }
        return super.removeById(id);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateById(PmsDept entity) {
        if (NumberUtil.equals(entity.getDeptId(), entity.getParentId())) {
            throw new BizException("上级部门不能是自己!");
        }
        var newParentDept = getById(entity.getParentId());
        var oldDept = getById(entity.getDeptId());
        if (Objects.nonNull(newParentDept) && Objects.nonNull(oldDept)) {
            var newAncestors = newParentDept.getAncestors() + StrUtil.COMMA + newParentDept.getDeptId();
            var oldAncestors = oldDept.getAncestors();
            entity.setAncestors(newAncestors);
            updateDeptChildren(entity.getDeptId(), newAncestors, oldAncestors);
        }
        return super.updateById(entity);
    }


    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        var children = baseMapper.selectChildDept(deptId);
        for (var child : children) {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (!children.isEmpty()) {
            baseMapper.updateChilds(children);
        }
    }


}
