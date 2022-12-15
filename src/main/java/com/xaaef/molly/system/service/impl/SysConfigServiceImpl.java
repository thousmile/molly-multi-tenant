package com.xaaef.molly.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaaef.molly.common.util.JsonUtils;
import com.xaaef.molly.core.redis.RedisCacheUtils;
import com.xaaef.molly.core.tenant.base.service.impl.BaseServiceImpl;
import com.xaaef.molly.system.entity.SysConfig;
import com.xaaef.molly.system.mapper.SysConfigMapper;
import com.xaaef.molly.system.service.SysConfigService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
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
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfigMapper, SysConfig>
        implements SysConfigService {

    private final RedisCacheUtils cacheUtils;


    @Override
    public boolean save(SysConfig entity) {
        if (!isMasterUser()) {
            throw new RuntimeException("非系统用户，不允许新增配置！");
        }
        return super.save(entity);
    }


    @Override
    public boolean updateById(SysConfig entity) {
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
    public boolean removeById(Serializable id) {
        if (!isMasterUser()) {
            throw new RuntimeException("非系统用户，不允许删除配置！");
        }
        var entity = super.getById(id);
        if (entity != null) {
            throw new RuntimeException("配置不存在！");
        }
        String key = String.format("%s:%s", REDIS_CACHE_KEY, entity.getConfigKey());
        cacheUtils.deleteKey(key);
        return super.removeById(entity.getConfigId());
    }


    @Override
    public String getValueByKey(String configKey) {
        String key = String.format("%s:%s", REDIS_CACHE_KEY, configKey);
        if (cacheUtils.hasKey(key)) {
            return cacheUtils.getString(key);
        }
        var wrapper = new LambdaQueryWrapper<SysConfig>()
                .select(SysConfig::getConfigValue)
                .eq(SysConfig::getConfigKey, configKey);
        var config = baseMapper.selectOne(wrapper);
        if (config != null && StringUtils.isNotBlank(config.getConfigValue())) {
            cacheUtils.setString(key, config.getConfigValue(), Duration.ofHours(1));
            return config.getConfigValue();
        }
        return null;
    }


    @Override
    public List<String> getValueListByKey(String configKey) {
        String str = getValueByKey(configKey);
        return JsonUtils.toListPojo(str, String.class);
    }


    @Override
    public Map<String, Object> getValueMapByKey(String configKey) {
        String str = getValueByKey(configKey);
        return JsonUtils.toMap(str, String.class, Object.class);
    }


    @Override
    public Integer getValueIntByKey(String configKey) {
        String str = getValueByKey(configKey);
        return Integer.parseInt(str);
    }


    @Override
    public Byte getValueByteByKey(String configKey) {
        String str = getValueByKey(configKey);
        return Byte.parseByte(str);
    }


    @Override
    public Double getValueDoubleByKey(String configKey) {
        String str = getValueByKey(configKey);
        return Double.parseDouble(str);
    }


    @Override
    public LocalDateTime getValueDateTimeByKey(String configKey) {
        String str = getValueByKey(configKey);
        return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN));
    }


    @Override
    public LocalTime getValueTimeByKey(String configKey) {
        String str = getValueByKey(configKey);
        return LocalTime.parse(str, DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN));
    }


    @Override
    public LocalDate getValueDateByKey(String configKey) {
        String str = getValueByKey(configKey);
        return LocalDate.parse(str, DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN));
    }


}
