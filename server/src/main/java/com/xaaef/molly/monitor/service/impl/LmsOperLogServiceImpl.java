package com.xaaef.molly.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.core.tenant.service.MultiTenantManager;
import com.xaaef.molly.core.tenant.util.TenantUtils;
import com.xaaef.molly.monitor.entity.LmsOperLog;
import com.xaaef.molly.monitor.mapper.LmsOperLogMapper;
import com.xaaef.molly.monitor.service.LmsOperLogService;
import com.xaaef.molly.perms.entity.PmsUser;
import com.xaaef.molly.perms.mapper.PmsUserMapper;
import com.xaaef.molly.perms.service.PmsUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * <p>
 * 操作日志
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/2/6 15:28
 */

@Slf4j
@Service
@AllArgsConstructor
public class LmsOperLogServiceImpl extends ServiceImpl<LmsOperLogMapper, LmsOperLog> implements LmsOperLogService {

    private final PmsUserMapper userMapper;

    private final MultiTenantManager tenantManager;


    @Override
    public IPage<LmsOperLog> pageKeywords(SearchPO params) {
        Page<LmsOperLog> pageRequest = Page.of(params.getPageIndex(), params.getPageSize());
        var wrapper = new LambdaQueryWrapper<LmsOperLog>();
        // 开始时间是否为空
        if (ObjectUtils.isNotEmpty(params.getStartDate())) {
            // 如果结束时间是否为空
            if (ObjectUtils.isNotEmpty(params.getEndDate())) {
                wrapper.between(LmsOperLog::getCreateTime, params.getStartDate(), params.getEndDate());
            } else {
                wrapper.between(LmsOperLog::getCreateTime, params.getStartDate(), LocalDate.now());
            }
        }
        if (StringUtils.isNotBlank(params.getKeywords())) {
            wrapper.like(LmsOperLog::getCreateTime, params.getKeywords());
        }
        wrapper.orderByDesc(LmsOperLog::getCreateTime);
        var result = super.page(pageRequest, wrapper);
        include(result.getRecords());
        return result;
    }


    private void include(List<LmsOperLog> list) {
        if (list.isEmpty()) {
            return;
        }
        var userIds = list.stream()
                .map(LmsOperLog::getUserId)
                .collect(Collectors.toSet());

        var wrapper = new LambdaQueryWrapper<PmsUser>()
                .select(PmsUser::getUserId, PmsUser::getUsername, PmsUser::getAvatar, PmsUser::getNickname)
                .in(PmsUser::getUserId, userIds);

        // 获取默认 租户ID 的用户
        var delegate1 = delegate(tenantManager.getDefaultTenantId(), () -> {
            return userMapper.selectList(wrapper)
                    .stream().collect(Collectors.toMap(PmsUser::getUserId, r -> r));
        });

        var mapUsers = new HashMap<Long, PmsUser>(delegate1);

        // 获取 非默认租户ID 的用户
        if (!tenantManager.isDefaultTenantId(TenantUtils.getTenantId())) {
            var delegate2 = delegate(TenantUtils.getTenantId(), () -> {
                return userMapper.selectList(wrapper)
                        .stream().collect(Collectors.toMap(PmsUser::getUserId, r -> r));
            });
            mapUsers.putAll(delegate2);
        }

        list.stream()
                .filter(f -> f.getUserId() != null)
                .forEach(r -> {
                    var user = mapUsers.get(r.getUserId());
                    r.setNickname(user.getNickname());
                    r.setUsername(user.getUsername());
                    r.setAvatar(user.getAvatar());
                });
    }


    private <S> S delegate(String targetTenant, Supplier<S> fun) {
        var originalTenantId = TenantUtils.getTenantId();
        TenantUtils.setTenantId(targetTenant);
        var result = fun.get();
        TenantUtils.setTenantId(originalTenantId);
        return result;
    }


}
