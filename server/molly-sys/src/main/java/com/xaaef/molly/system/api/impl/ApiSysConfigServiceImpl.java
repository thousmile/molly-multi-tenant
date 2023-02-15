package com.xaaef.molly.system.api.impl;

import com.xaaef.molly.internal.api.ApiSysConfigService;
import com.xaaef.molly.system.service.SysConfigService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/2/14 14:02
 */


@Slf4j
@Service
@AllArgsConstructor
public class ApiSysConfigServiceImpl implements ApiSysConfigService {

    private final SysConfigService configService;


    @Override
    public String getValueByKey(String configKey) {
        return configService.getValueByKey(configKey);
    }

    @Override
    public List<String> getValueListByKey(String configKey) {
        return configService.getValueListByKey(configKey);
    }

    @Override
    public Map<String, Object> getValueMapByKey(String configKey) {
        return configService.getValueMapByKey(configKey);
    }

    @Override
    public Integer getValueIntByKey(String configKey) {
        return configService.getValueIntByKey(configKey);
    }

    @Override
    public Byte getValueByteByKey(String configKey) {
        return configService.getValueByteByKey(configKey);
    }

    @Override
    public Double getValueDoubleByKey(String configKey) {
        return configService.getValueDoubleByKey(configKey);
    }

    @Override
    public LocalDateTime getValueDateTimeByKey(String configKey) {
        return configService.getValueDateTimeByKey(configKey);
    }

    @Override
    public LocalTime getValueTimeByKey(String configKey) {
        return configService.getValueTimeByKey(configKey);
    }

    @Override
    public LocalDate getValueDateByKey(String configKey) {
        return configService.getValueDateByKey(configKey);
    }


}
