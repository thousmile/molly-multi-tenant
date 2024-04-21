package com.xaaef.molly.system.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaaef.molly.common.consts.ConfigDataConst;
import com.xaaef.molly.common.exception.BizException;
import com.xaaef.molly.common.util.JsonUtils;
import com.xaaef.molly.system.entity.SysConfig;
import com.xaaef.molly.system.mapper.SysConfigMapper;
import com.xaaef.molly.system.service.SysConfigService;
import com.xaaef.molly.tenant.base.service.impl.BaseServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.xaaef.molly.auth.jwt.JwtSecurityUtils.isAdminUser;
import static com.xaaef.molly.auth.jwt.JwtSecurityUtils.isMasterUser;

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
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    private final StringRedisTemplate redisTemplate;

    private HashOperations<String, Object, Object> hash() {
        return redisTemplate.opsForHash();
    }


    @PostConstruct
    public void init() {
        var wrapper = new LambdaQueryWrapper<SysConfig>()
                .select(SysConfig::getConfigKey, SysConfig::getConfigValue);
        var configMaps = baseMapper.selectList(wrapper)
                .stream()
                .collect(Collectors.toMap(SysConfig::getConfigKey, SysConfig::getConfigValue));
        hash().putAll(ConfigDataConst.REDIS_CACHE_KEY, configMaps);
        log.info("SysConfig init success....");
    }


    @PreDestroy
    public void destroy() {
        redisTemplate.delete(ConfigDataConst.REDIS_CACHE_KEY);
        log.info("SysConfig destroy success....");
    }


    @Override
    public boolean save(SysConfig entity) {
        if (isMasterUser() && isAdminUser()) {
            if (super.exist(SysConfig::getConfigKey, entity.getConfigKey())) {
                throw new BizException(String.format("参数键名 %s 已经存在了！", entity.getConfigKey()));
            }
            hash().put(ConfigDataConst.REDIS_CACHE_KEY, entity.getConfigKey(), entity.getConfigValue());
            return super.save(entity);
        }
        throw new BizException("非系统管理员用户，不允许新增配置！");
    }


    @Override
    public boolean updateById(SysConfig entity) {
        if (isMasterUser() && isAdminUser()) {
            var wrapper = new LambdaQueryWrapper<SysConfig>()
                    .eq(SysConfig::getConfigKey, entity.getConfigKey())
                    .ne(SysConfig::getConfigId, entity.getConfigId());
            if (super.count(wrapper) > 0) {
                throw new BizException(String.format("参数键名 %s 已经存在了！", entity.getConfigKey()));
            }
            if (StringUtils.isNotBlank(entity.getConfigValue())) {
                hash().put(ConfigDataConst.REDIS_CACHE_KEY, entity.getConfigKey(), entity.getConfigValue());
            }
            return super.updateById(entity);
        }
        throw new BizException("非系统管理员用户，不允许修改配置！");
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeById(Serializable id) {
        if (isMasterUser() && isAdminUser()) {
            var entity = super.getById(id);
            if (entity == null) {
                throw new BizException("配置不存在！");
            }
            hash().delete(ConfigDataConst.REDIS_CACHE_KEY, entity.getConfigKey());
            return super.removeById(entity.getConfigId());
        }
        throw new BizException("非系统管理员用户，不允许删除配置！");
    }


    @Override
    public String getValueByKey(String configKey) {
        var flag = hash().hasKey(ConfigDataConst.REDIS_CACHE_KEY, configKey);
        if (Boolean.TRUE.equals(flag)) {
            var obj = hash().get(ConfigDataConst.REDIS_CACHE_KEY, configKey);
            return (String) obj;
        }
        var wrapper = new LambdaQueryWrapper<SysConfig>()
                .select(SysConfig::getConfigValue)
                .eq(SysConfig::getConfigKey, configKey);
        var config = baseMapper.selectOne(wrapper);
        if (config != null && StringUtils.isNotBlank(config.getConfigValue())) {
            hash().put(ConfigDataConst.REDIS_CACHE_KEY, configKey, config.getConfigValue());
            return config.getConfigValue();
        }
        return StrUtil.EMPTY;
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
        return LocalDateTime.parse(str, DatePattern.NORM_DATETIME_FORMATTER);
    }


    @Override
    public LocalTime getValueTimeByKey(String configKey) {
        String str = getValueByKey(configKey);
        return LocalTime.parse(str, DatePattern.NORM_TIME_FORMATTER);
    }


    @Override
    public LocalDate getValueDateByKey(String configKey) {
        String str = getValueByKey(configKey);
        return LocalDate.parse(str, DatePattern.NORM_DATE_FORMATTER);
    }

}
