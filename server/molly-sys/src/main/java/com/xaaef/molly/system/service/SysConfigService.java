package com.xaaef.molly.system.service;

import com.xaaef.molly.system.entity.SysConfig;
import com.xaaef.molly.tenant.base.service.BaseService;

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
 * @date 2022/12/9 15:33
 */


public interface SysConfigService extends BaseService<SysConfig> {


    /**
     * 根据 configKey 查看配置信息,，返回 String
     *
     * @param configKey
     * @return SysConfig
     * @author Wang Chen Chen
     * @date 2021/7/16 11:22
     */
    String getValueByKey(String configKey);


    /**
     * 根据 configKey 查看配置信息，返回 List
     *
     * @param configKey
     * @return SysConfig
     * @author Wang Chen Chen
     * @date 2021/7/16 11:22
     */
    List<String> getValueListByKey(String configKey);


    /**
     * 根据 configKey 查看配置信息，返回 Map
     *
     * @param configKey
     * @return SysConfig
     * @author Wang Chen Chen
     * @date 2021/7/16 11:22
     */
    Map<String, Object> getValueMapByKey(String configKey);


    /**
     * 根据 configKey 查看配置信息，返回 Integer
     *
     * @param configKey
     * @return SysConfig
     * @author Wang Chen Chen
     * @date 2021/7/16 11:22
     */
    Integer getValueIntByKey(String configKey);


    /**
     * 根据 configKey 查看配置信息，返回 Byte
     *
     * @param configKey
     * @return SysConfig
     * @author Wang Chen Chen
     * @date 2021/7/16 11:22
     */
    Byte getValueByteByKey(String configKey);



    /**
     * 根据 configKey 查看配置信息，返回 Double
     *
     * @param configKey
     * @return SysConfig
     * @author Wang Chen Chen
     * @date 2021/7/16 11:22
     */
    Double getValueDoubleByKey(String configKey);


    /**
     * 根据 configKey 查看配置信息，返回 LocalDateTime
     *
     * @param configKey
     * @return SysConfig
     * @author Wang Chen Chen
     * @date 2021/7/16 11:22
     */
    LocalDateTime getValueDateTimeByKey(String configKey);

    /**
     * 根据 configKey 查看配置信息 ，返回 LocalTime
     *
     * @param configKey
     * @return SysConfig
     * @author Wang Chen Chen
     * @date 2021/7/16 11:22
     */
    LocalTime getValueTimeByKey(String configKey);

    /**
     * 根据 configKey 查看配置信息，返回 LocalDate
     *
     * @param configKey
     * @return SysConfig
     * @author Wang Chen Chen
     * @date 2021/7/16 11:22
     */
    LocalDate getValueDateByKey(String configKey);


}
