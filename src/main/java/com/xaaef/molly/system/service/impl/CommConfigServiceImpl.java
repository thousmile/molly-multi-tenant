package com.xaaef.molly.system.service.impl;

import com.xaaef.molly.common.util.JsonUtils;
import com.xaaef.molly.core.redis.RedisCacheUtils;
import com.xaaef.molly.core.tenant.base.service.impl.BaseServiceImpl;
import com.xaaef.molly.system.entity.CommConfig;
import com.xaaef.molly.system.repository.CommConfigRepository;
import com.xaaef.molly.system.service.CommConfigService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static com.xaaef.molly.common.consts.ConfigName.REDIS_CACHE_KEY;
import static com.xaaef.molly.common.util.JsonUtils.*;
import static com.xaaef.molly.core.auth.jwt.JwtSecurityUtils.isMasterUser;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/9 15:36
 */


@Slf4j
@Service
@AllArgsConstructor
public class CommConfigServiceImpl extends BaseServiceImpl<CommConfigRepository, CommConfig, Long>
        implements CommConfigService {

    private final RedisCacheUtils cacheUtils;

    @Override
    public CommConfig save(CommConfig entity) {
        if (!isMasterUser()) {
            throw new RuntimeException("非系统用户，不允许新增配置！");
        }
        return super.save(entity);
    }


    @Override
    public CommConfig updateById(CommConfig entity) {
        if (!isMasterUser()) {
            throw new RuntimeException("非系统用户，不允许修改配置！");
        }
        if (StringUtils.isNotBlank(entity.getConfigValue())) {
            String key = String.format("%s:%s", REDIS_CACHE_KEY, entity.getConfigKey());
            cacheUtils.setString(key, entity.getConfigValue(), Duration.ofHours(1));
        }
        return super.updateById(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long id) {
        if (!isMasterUser()) {
            throw new RuntimeException("非系统用户，不允许删除配置！");
        }
        var optional = super.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("配置不存在！");
        }
        var entity = optional.get();
        String key = String.format("%s:%s", REDIS_CACHE_KEY, entity.getConfigKey());
        cacheUtils.deleteKey(key);
        super.deleteById(entity.getConfigId());
    }


    @Override
    public String findValueByKey(String configKey) {
        String key = String.format("%s:%s", REDIS_CACHE_KEY, configKey);
        if (cacheUtils.hasKey(key)) {
            return cacheUtils.getString(key);
        }
        var configValue = baseReps.findOneByConfigKey(configKey);
        if (StringUtils.isNotBlank(configValue)) {
            cacheUtils.setString(key, configValue, Duration.ofHours(1));
            return configValue;
        }
        return null;
    }


    @Override
    public List<String> findValueListByKey(String configKey) {
        String str = findValueByKey(configKey);
        return JsonUtils.toListPojo(str, String.class);
    }

    @Override
    public Map<String, Object> findValueMapByKey(String configKey) {
        String str = findValueByKey(configKey);
        return JsonUtils.toMap(str, String.class, Object.class);
    }

    @Override
    public Integer findValueIntByKey(String configKey) {
        String str = findValueByKey(configKey);
        return Integer.parseInt(str);
    }

    @Override
    public Double findValueDoubleByKey(String configKey) {
        String str = findValueByKey(configKey);
        return Double.parseDouble(str);
    }

    @Override
    public LocalDateTime findValueDateTimeByKey(String configKey) {
        String str = findValueByKey(configKey);
        return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN));
    }

    @Override
    public LocalTime findValueTimeByKey(String configKey) {
        String str = findValueByKey(configKey);
        return LocalTime.parse(str, DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN));
    }

    @Override
    public LocalDate findValueDateByKey(String configKey) {
        String str = findValueByKey(configKey);
        return LocalDate.parse(str, DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN));
    }


}
