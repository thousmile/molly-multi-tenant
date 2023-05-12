package com.xaaef.molly.perms.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaaef.molly.common.enums.StatusEnum;
import com.xaaef.molly.common.util.IdUtils;
import com.xaaef.molly.internal.api.ApiSysConfigService;
import com.xaaef.molly.internal.api.ApiPmsUserService;
import com.xaaef.molly.internal.dto.InitUserDTO;
import com.xaaef.molly.internal.dto.PmsUserDTO;
import com.xaaef.molly.perms.entity.PmsDept;
import com.xaaef.molly.perms.entity.PmsRole;
import com.xaaef.molly.perms.entity.PmsUser;
import com.xaaef.molly.perms.mapper.PmsDeptMapper;
import com.xaaef.molly.perms.mapper.PmsRoleMapper;
import com.xaaef.molly.perms.mapper.PmsUserMapper;
import com.xaaef.molly.perms.service.PmsUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xaaef.molly.auth.jwt.JwtSecurityUtils.encryptPassword;
import static com.xaaef.molly.common.consts.ConfigName.TENANT_DEFAULT_ROLE_NAME;
import static com.xaaef.molly.common.enums.AdminFlag.YES;
import static com.xaaef.molly.common.enums.GenderType.MALE;
import static com.xaaef.molly.tenant.util.DelegateUtils.*;


/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/2/14 11:47
 */


@Slf4j
@Service
@AllArgsConstructor
public class ApiPmsUserServiceImpl implements ApiPmsUserService {

    private final ApiSysConfigService configService;

    private final PmsUserMapper userMapper;

    private final PmsDeptMapper deptMapper;

    private final PmsRoleMapper roleMapper;


    @Override
    public PmsUserDTO getByUsername(String username) {
        var wrapper = new LambdaQueryWrapper<PmsUser>()
                .eq(PmsUser::getUsername, username);
        return toBuilder(userMapper.selectOne(wrapper));
    }


    @Override
    public PmsUserDTO getByMobile(String mobile) {
        var wrapper = new LambdaQueryWrapper<PmsUser>()
                .eq(PmsUser::getMobile, mobile);
        return toBuilder(userMapper.selectOne(wrapper));
    }


    @Override
    public PmsUserDTO getByEmail(String email) {
        var wrapper = new LambdaQueryWrapper<PmsUser>()
                .eq(PmsUser::getEmail, email);
        return toBuilder(userMapper.selectOne(wrapper));
    }


    @Override
    public PmsUserDTO getByUserId(Long userId) {
        return toBuilder(userMapper.selectById(userId));
    }


    @Override
    public Set<PmsUserDTO> listByUserIds(Set<Long> userIds) {
        var wrapper = new LambdaQueryWrapper<PmsUser>()
                .select(PmsUser::getUserId, PmsUser::getUsername, PmsUser::getAvatar, PmsUser::getNickname)
                .in(PmsUser::getUserId, userIds);
        return userMapper.selectList(wrapper)
                .stream()
                .map(this::toBuilder)
                .collect(Collectors.toSet());
    }


    @Override
    public boolean existByUserId(Long userId) {
        return userMapper.exists(
                new LambdaQueryWrapper<PmsUser>()
                        .eq(PmsUser::getUserId, userId)
        );
    }


    @Override
    public void initUserAndRoleAndDept(InitUserDTO po) {
        // 租户默认角色名称
        var roleName = Optional.ofNullable(configService.getValueByKey(TENANT_DEFAULT_ROLE_NAME))
                .orElse("管理员");

        // 委托，新的租户id。执行初始化数据
        delegate(po.getTenantId(), () -> {

            var pmsDept = new PmsDept()
                    .setParentId(0L)
                    .setDeptName(po.getName())
                    .setLeader(po.getAdminNickname())
                    .setLeaderMobile(po.getAdminMobile())
                    .setSort(1L)
                    .setDescription(po.getName())
                    .setAncestors("0");

            // 保存部门
            deptMapper.insert(pmsDept);

            var pmsRole = new PmsRole()
                    .setRoleName(String.format("%s %s", po.getName(), roleName))
                    .setSort(1L);
            pmsRole.setDescription(pmsRole.getRoleName());

            // 保存角色
            roleMapper.insert(pmsRole);

            var password = encryptPassword(po.getAdminPwd());

            var pmsUser = new PmsUser()
                    .setUserId(IdUtils.getStandaloneId())
                    .setAvatar(po.getLogo())
                    .setUsername(po.getAdminUsername())
                    .setMobile(po.getAdminMobile())
                    .setEmail(po.getAdminEmail())
                    .setNickname(po.getAdminNickname())
                    .setPassword(password)
                    .setGender(MALE.getCode())
                    .setAdminFlag(YES.getCode())
                    .setStatus(StatusEnum.NORMAL.getCode())
                    .setDeptId(pmsDept.getDeptId())
                    .setRoles(Set.of(pmsRole));

            userMapper.insert(pmsUser);

            // 保存关联
            userMapper.insertByRoles(pmsUser.getUserId(), Set.of(pmsRole.getRoleId()));

            return true;
        });
    }


    private PmsUserDTO toBuilder(PmsUser source) {
        if (source == null) {
            return null;
        }
        var target = new PmsUserDTO();
        BeanUtils.copyProperties(source, target);
        return target;
    }

}
