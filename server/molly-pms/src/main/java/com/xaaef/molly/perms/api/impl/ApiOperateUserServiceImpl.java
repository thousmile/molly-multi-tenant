package com.xaaef.molly.perms.api.impl;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaaef.molly.common.consts.MbpConst;
import com.xaaef.molly.internal.api.ApiOperateUserService;
import com.xaaef.molly.internal.api.ApiSysTenantService;
import com.xaaef.molly.internal.dto.OperateUserDTO;
import com.xaaef.molly.perms.entity.PmsUser;
import com.xaaef.molly.perms.mapper.PmsUserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.xaaef.molly.tenant.util.DelegateUtils.delegate;

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
    public void reflectionFill(Object objList) {
        if (objList instanceof Collection<?> list) {
            var userIds = new HashSet<Long>();
            list.forEach(obj -> {
                if (ReflectUtil.getFieldValue(obj, MbpConst.ATTR_CREATE_USER) instanceof Long createUserId) {
                    userIds.add(createUserId);
                }
                if (ReflectUtil.getFieldValue(obj, MbpConst.ATTR_LAST_UPDATE_USER) instanceof Long lastUpdateUserId) {
                    userIds.add(lastUpdateUserId);
                }
            });
            if (!userIds.isEmpty()) {
                Map<Long, OperateUserDTO> operateUserMaps = mapOperateUser(userIds);
                if (!operateUserMaps.isEmpty()) {
                    list.forEach(obj -> {
                        if (ReflectUtil.getFieldValue(obj, MbpConst.ATTR_CREATE_USER) instanceof Long createUserId) {
                            var operateUser = operateUserMaps.get(createUserId);
                            ReflectUtil.setFieldValue(obj, MbpConst.ATTR_CREATE_USER + "Entity", operateUser);
                        }
                        if (ReflectUtil.getFieldValue(obj, MbpConst.ATTR_LAST_UPDATE_USER) instanceof Long lastUpdateUserId) {
                            var operateUser = operateUserMaps.get(lastUpdateUserId);
                            ReflectUtil.setFieldValue(obj, MbpConst.ATTR_LAST_UPDATE_USER + "Entity", operateUser);
                        }
                    });
                }
            }
        }
    }

    @Override
    public Map<Long, OperateUserDTO> mapOperateUser(Set<Long> userIds) {
        var operateUserMaps = new HashMap<Long, OperateUserDTO>();
        // 从 默认租户中，获取 用户信息
        var defaultTenantId = apiSysTenantService.getByDefaultTenantId();
        var defaultTenantUser = delegate(defaultTenantId, () -> listOperateUser(userIds));
        if (!defaultTenantUser.isEmpty()) {
            operateUserMaps.putAll(defaultTenantUser);
        }
        // 从 当前租户中，获取 用户信息
        var currentTenantUser = listOperateUser(userIds);
        if (!currentTenantUser.isEmpty()) {
            operateUserMaps.putAll(currentTenantUser);
        }
        return operateUserMaps;
    }

    private Map<Long, OperateUserDTO> listOperateUser(Set<Long> userIds) {
        var wrapper = new LambdaQueryWrapper<PmsUser>()
                .select(List.of(PmsUser::getUserId, PmsUser::getAvatar, PmsUser::getNickname))
                .in(PmsUser::getUserId, userIds);
        return userMapper.selectList(wrapper)
                .stream()
                .map(p -> new OperateUserDTO(p.getUserId(), p.getAvatar(), p.getNickname()))
                .collect(Collectors.toMap(OperateUserDTO::getUserId, p -> p));
    }

}
