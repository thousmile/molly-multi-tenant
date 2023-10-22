package com.xaaef.molly.perms.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaaef.molly.internal.api.ApiPmsDeptService;
import com.xaaef.molly.internal.dto.PmsDeptDTO;
import com.xaaef.molly.perms.entity.PmsDept;
import com.xaaef.molly.perms.service.PmsDeptService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/10/21 11:48
 */

@Slf4j
@Service
@AllArgsConstructor
public class ApiPmsDeptServiceImpl implements ApiPmsDeptService {

    private final PmsDeptService deptService;

    @Override
    public Map<Long, PmsDeptDTO> mapByDeptIds(Set<Long> deptIds) {
        var wrapper = new LambdaQueryWrapper<PmsDept>()
                .select(
                        List.of(
                                PmsDept::getDeptId, PmsDept::getParentId, PmsDept::getDeptName,
                                PmsDept::getLeader, PmsDept::getLeaderMobile, PmsDept::getSort,
                                PmsDept::getDescription, PmsDept::getAncestors
                        )
                )
                .in(PmsDept::getDeptId, deptIds);
        return deptService.list(wrapper)
                .stream()
                .map(source -> {
                    var target = new PmsDeptDTO();
                    BeanUtils.copyProperties(source, target);
                    return target;
                })
                .collect(Collectors.toMap(PmsDeptDTO::getDeptId, a -> a));
    }


    @Override
    public Set<Long> listChildIdByDeptId(Long deptId) {
        return deptService.listChildIdByDeptId(deptId);
    }


    @Override
    public Set<PmsDeptDTO> listChildByDeptId(Long deptId) {
        return deptService.listChildByDeptId(deptId)
                .stream()
                .map(source -> {
                    var target = new PmsDeptDTO();
                    BeanUtils.copyProperties(source, target);
                    return target;
                })
                .collect(Collectors.toSet());
    }

}
