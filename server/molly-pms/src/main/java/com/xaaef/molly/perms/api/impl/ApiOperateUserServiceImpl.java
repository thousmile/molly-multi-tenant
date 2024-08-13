package com.xaaef.molly.perms.api.impl;

import com.xaaef.molly.common.util.TenantUtils;
import com.xaaef.molly.internal.api.ApiOperateUserService;
import com.xaaef.molly.internal.api.ApiSysTenantService;
import com.xaaef.molly.internal.dto.OperateUserDTO;
import com.xaaef.molly.perms.mapper.PmsUserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 反射 填充 操作用户信息
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/10/31 10:53
 */


@Slf4j
@Service
@AllArgsConstructor
public class ApiOperateUserServiceImpl implements ApiOperateUserService {

    private final ApiSysTenantService apiSysTenantService;

    private final PmsUserMapper userMapper;

    @Override
    public Map<Long, OperateUserDTO> mapOperateUser(Set<Long> userIds) {
        final var props = apiSysTenantService.getByMultiTenantProperties();
        final var dbNameList = new HashSet<>(Set.of(props.getPrefix() + props.getDefaultTenantId()));
        dbNameList.add(props.getPrefix() + TenantUtils.getTenantId());
        return userMapper.selectSimpleListByUserIds(dbNameList, userIds)
                .stream()
                .map(a -> new OperateUserDTO()
                        .setUserId(a.getUserId())
                        .setAvatar(a.getAvatar())
                        .setNickname(a.getNickname())
                )
                .collect(Collectors.toMap(OperateUserDTO::getUserId, o -> o));
    }

}
