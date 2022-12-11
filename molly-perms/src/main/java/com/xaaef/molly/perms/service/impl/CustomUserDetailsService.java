package com.xaaef.molly.perms.service.impl;

import cn.hutool.core.util.StrUtil;
import com.xaaef.molly.core.auth.enums.StatusEnum;
import com.xaaef.molly.perms.entity.SysUser;
import com.xaaef.molly.perms.repository.SysUserRepository;
import com.xaaef.molly.core.auth.jwt.StringGrantedAuthority;
import com.xaaef.molly.core.auth.jwt.JwtLoginUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;


@Slf4j
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SysUserRepository userReps;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var one = userReps.findOne(
                Example.of(SysUser.builder().username(username).build())
        );
        if (one.isEmpty()) {
            throw new UsernameNotFoundException(
                    StrUtil.format("用户名 {} 不存在！", username)
            );
        }
        var dbUser = one.get();
        // 获取此用户的权限列表
/*        var authorities = permissionService.listSimpleByUserId(dbUser.getUserId())
                .stream()
                .map(StringGrantedAuthority::new)
                .collect(Collectors.toSet());*/
        return JwtLoginUser.builder()
                .userId(dbUser.getUserId())
                .avatar(dbUser.getAvatar())
                .username(dbUser.getUsername())
                .nickname(dbUser.getNickname())
                .password(dbUser.getPassword())
                .status(StatusEnum.create(dbUser.getStatus()))
                .authorities(Set.of())
                .build();
    }


}
