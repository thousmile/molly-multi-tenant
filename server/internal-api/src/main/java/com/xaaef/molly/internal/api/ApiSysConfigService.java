package com.xaaef.molly.internal.api;

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
 * @date 2023/2/14 10:17
 */

public interface ApiSysConfigService {


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
