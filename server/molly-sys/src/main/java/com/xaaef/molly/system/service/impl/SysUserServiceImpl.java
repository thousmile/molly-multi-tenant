package com.xaaef.molly.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaaef.molly.auth.service.JwtTokenService;
import com.xaaef.molly.common.exception.BizException;
import com.xaaef.molly.internal.api.ApiPmsUserService;
import com.xaaef.molly.system.entity.SysTenant;
import com.xaaef.molly.system.mapper.SysTenantMapper;
import com.xaaef.molly.system.mapper.SysUserMapper;
import com.xaaef.molly.system.service.SysUserService;
import com.xaaef.molly.system.vo.UserListTenantVO;
import com.xaaef.molly.tenant.service.MultiTenantManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xaaef.molly.auth.jwt.JwtSecurityUtils.*;
import static com.xaaef.molly.tenant.util.DelegateUtils.delegate;


@Slf4j
@Service
@AllArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper baseMapper;

    private final ApiPmsUserService pmsUserService;

    private final MultiTenantManager tenantManager;

    private final SysTenantMapper tenantMapper;

    private final JwtTokenService jwtTokenService;


    @Override
    public Set<String> listHaveTenantIds(Long userId) {
        return delegate(tenantManager.getDefaultTenantId(),
                () -> baseMapper.selectHaveTenants(userId)
        );
    }


    @Override
    public UserListTenantVO listHaveTenants(Long userId) {
        var tenantIds = listHaveTenantIds(userId);
        var wrapper = new LambdaQueryWrapper<SysTenant>()
                .select(
                        SysTenant::getTenantId,
                        SysTenant::getName
                );
        var tenants = tenantMapper.selectList(wrapper)
                .stream()
                .map(r -> new UserListTenantVO.Entry(r.getTenantId(), r.getName()))
                .collect(Collectors.toSet());
        return new UserListTenantVO()
                .setHave(tenantIds)
                .setAll(tenants);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateTenant(Long userId, Set<String> tenantIds) {
        // 只有 系统用户，并且是管理员，才可以给其他用户分配租户
        if (isMasterUser() && isAdminUser()) {
            // 系统管理员 无需分配租户。关联全部租户
            if (Objects.equals(getUserId(), userId)) {
                return true;
            }
            // 获取存在的 租户ID
            var collect = tenantIds.stream().filter(tenantManager::existById).collect(Collectors.toSet());
            return delegate(tenantManager.getDefaultTenantId(), () -> {
                var sysUser = pmsUserService.getByUserId(userId);
                // 判断 系统用户 是否存在
                if (sysUser == null) {
                    throw new BizException(String.format("系统用户 %s 不存在！", userId));
                }
                // 删除 系统用户 之前关联的租户
                baseMapper.deleteHaveTenants(userId);
                // 根据 用户名，获取登录的用户信息
                var loginUser = jwtTokenService.getLoginUserByUsername(sysUser.getUsername());
                if (loginUser != null) {
                    // 更新 系统用户 关联的租户ID
                    loginUser.setHaveTenantIds(collect);
                    jwtTokenService.updateLoginUser(loginUser);
                }
                if (!collect.isEmpty()) {
                    // 系统用户 关联 新的租户
                    var r2 = baseMapper.insertByTenants(userId, collect);
                    return r2 > 0;
                }
                return true;
            });
        }
        throw new BizException("只有系统管理员，才能给系统用户分配租户！");
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteHaveSysUser(String tenantId) {
        return baseMapper.deleteHaveSysUser(tenantId) > 0;
    }


}
