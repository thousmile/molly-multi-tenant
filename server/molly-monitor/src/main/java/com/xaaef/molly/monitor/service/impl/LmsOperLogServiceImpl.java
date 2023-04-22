package com.xaaef.molly.monitor.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.monitor.repository.LmsOperLogRepository;
import com.xaaef.molly.tenant.service.MultiTenantManager;

import com.xaaef.molly.common.util.TenantUtils;
import com.xaaef.molly.internal.api.ApiPmsUserService;
import com.xaaef.molly.internal.dto.PmsUserDTO;
import com.xaaef.molly.monitor.entity.LmsOperLog;
import com.xaaef.molly.monitor.service.LmsOperLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xaaef.molly.tenant.util.DelegateUtils.*;

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
public class LmsOperLogServiceImpl implements LmsOperLogService {

    private final LmsOperLogRepository operLogRepository;

    private final ApiPmsUserService userService;

    private final MultiTenantManager tenantManager;


    @Override
    public IPage<LmsOperLog> pageKeywords(SearchPO params) {
        Page<LmsOperLog> pageRequest = Page.of(params.getPageIndex(), params.getPageSize());
        var wrapper = new LambdaQueryWrapper<LmsOperLog>();
        // 开始时间是否为空
        if (ObjectUtil.isNotEmpty(params.getStartDate())) {
            // 如果结束时间是否为空
            if (ObjectUtil.isNotEmpty(params.getEndDate())) {
                wrapper.between(LmsOperLog::getCreateTime, params.getStartDate(), params.getEndDate());
            } else {
                wrapper.between(LmsOperLog::getCreateTime, params.getStartDate(), LocalDate.now());
            }
        }
        if (StringUtils.isNotBlank(params.getKeywords())) {
            wrapper.like(LmsOperLog::getCreateTime, params.getKeywords());
        }
        wrapper.orderByDesc(LmsOperLog::getCreateTime);
        return null;
    }


    @Override
    public boolean removeBatchByIds(Set<String> ids) {
        operLogRepository.deleteAllById(ids);
        return true;
    }


    private void include(List<LmsOperLog> list) {
        if (list.isEmpty()) {
            return;
        }
        var userIds = list.stream()
                .map(LmsOperLog::getUserId)
                .collect(Collectors.toSet());

        // 获取默认 租户ID 的用户
        var delegate1 = delegate(tenantManager.getDefaultTenantId(), () -> {
            return userService.listByUserIds(userIds)
                    .stream().collect(Collectors.toMap(PmsUserDTO::getUserId, r -> r));
        });

        var mapUsers = new HashMap<Long, PmsUserDTO>(delegate1);

        // 获取 非默认租户ID 的用户
        if (!tenantManager.isDefaultTenantId(TenantUtils.getTenantId())) {
            var delegate2 = delegate(TenantUtils.getTenantId(), () -> {
                return userService.listByUserIds(userIds)
                        .stream().collect(Collectors.toMap(PmsUserDTO::getUserId, r -> r));
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


}
