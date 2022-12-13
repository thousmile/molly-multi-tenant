package com.xaaef.molly.system.service;

import com.xaaef.molly.core.tenant.base.service.BaseService;
import com.xaaef.molly.system.entity.CommConfig;

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


public interface CommConfigService extends BaseService<CommConfig, Long> {


    /**
     * 根据 configKey 查看配置信息,，返回 String
     *
     * @param configKey
     * @return SysConfig
     * @author Wang Chen Chen
     * @date 2021/7/16 11:22
     */
    String findValueByKey(String configKey);


    /**
     * 根据 configKey 查看配置信息，返回 List
     *
     * @param configKey
     * @return SysConfig
     * @author Wang Chen Chen
     * @date 2021/7/16 11:22
     */
    List<String> findValueListByKey(String configKey);


    /**
     * 根据 configKey 查看配置信息，返回 Map
     *
     * @param configKey
     * @return SysConfig
     * @author Wang Chen Chen
     * @date 2021/7/16 11:22
     */
    Map<String, Object> findValueMapByKey(String configKey);


    /**
     * 根据 configKey 查看配置信息，返回 Integer
     *
     * @param configKey
     * @return SysConfig
     * @author Wang Chen Chen
     * @date 2021/7/16 11:22
     */
    Integer findValueIntByKey(String configKey);

    /**
     * 根据 configKey 查看配置信息，返回 Double
     *
     * @param configKey
     * @return SysConfig
     * @author Wang Chen Chen
     * @date 2021/7/16 11:22
     */
    Double findValueDoubleByKey(String configKey);


    /**
     * 根据 configKey 查看配置信息，返回 LocalDateTime
     *
     * @param configKey
     * @return SysConfig
     * @author Wang Chen Chen
     * @date 2021/7/16 11:22
     */
    LocalDateTime findValueDateTimeByKey(String configKey);

    /**
     * 根据 configKey 查看配置信息 ，返回 LocalTime
     *
     * @param configKey
     * @return SysConfig
     * @author Wang Chen Chen
     * @date 2021/7/16 11:22
     */
    LocalTime findValueTimeByKey(String configKey);

    /**
     * 根据 configKey 查看配置信息，返回 LocalDate
     *
     * @param configKey
     * @return SysConfig
     * @author Wang Chen Chen
     * @date 2021/7/16 11:22
     */
    LocalDate findValueDateByKey(String configKey);


}
