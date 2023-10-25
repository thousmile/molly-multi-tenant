package com.xaaef.molly.perms.api.impl;

import com.xaaef.molly.internal.api.ApiPmsRoleService;
import com.xaaef.molly.internal.dto.PmsRoleDTO;
import com.xaaef.molly.perms.mapper.PmsRoleMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

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
public class ApiPmsRoleServiceImpl implements ApiPmsRoleService {

    private final PmsRoleMapper roleMapper;


    @Override
    public Set<PmsRoleDTO> listByUserId(Long userId) {
        return roleMapper.selectListByUserIds(Set.of(userId))
                .stream()
                .map(r -> new PmsRoleDTO()
                        .setRoleId(r.getRoleId())
                        .setRoleName(r.getRoleName())
                        .setDescription(r.getDescription())
                ).collect(Collectors.toSet());
    }


    @Override
    public Set<Long> listMenuIdByRoleIds(Set<Long> roleIds) {
        return roleMapper.selectMenuIdByRoleIds(roleIds);
    }


}
