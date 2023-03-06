package com.xaaef.molly.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaaef.molly.auth.jwt.JwtLoginUser;
import com.xaaef.molly.auth.service.JwtTokenService;
import com.xaaef.molly.internal.api.ApiPmsUserService;
import com.xaaef.molly.internal.dto.PmsUserDTO;
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

import java.util.Set;
import java.util.stream.Collectors;


import static com.xaaef.molly.auth.jwt.JwtSecurityUtils.getTenantId;
import static com.xaaef.molly.auth.jwt.JwtSecurityUtils.isMasterUser;
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
        return delegate(tenantManager.getDefaultTenantId(), () -> {
            return baseMapper.selectHaveTenants(userId);
        });
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
        if (!isMasterUser()) {
            throw new RuntimeException("只有系统用户，才能给用户关联租户！");
        }
        // 获取存在的 租户ID
        var collect = tenantIds.stream().filter(tenantManager::existById).collect(Collectors.toSet());
        return delegate(tenantManager.getDefaultTenantId(), () -> {
            var sysUser = pmsUserService.getByUserId(userId);
            // 判断系统用户是否存在
            if (sysUser != null) {
                // 删除 用户 之前关联的租户
                baseMapper.deleteHaveTenants(userId);
                // 根据用户名，获取 登录的用户信息
                var loginUser = jwtTokenService.getLoginUserByUsername(getTenantId(), sysUser.getUsername());
                if (loginUser != null) {
                    // 更新 系统用户 关联的租户ID
                    loginUser.setHaveTenantIds(collect);
                    jwtTokenService.updateLoginUser(loginUser);
                }
                if (!collect.isEmpty()) {
                    // 用户 关联 新的租户
                    var r2 = baseMapper.insertByTenants(userId, collect);
                    return r2 > 0;
                }
                return true;
            }
            return false;
        });
    }


}
