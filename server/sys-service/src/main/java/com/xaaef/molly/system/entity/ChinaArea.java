package com.xaaef.molly.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;


/**
 * <p>
 * 中国行政区域
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */


@TableName("china_area")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ChinaArea implements java.io.Serializable {

    /**
     * 行政代码 [ 唯一 ]
     */
    @TableId
    private Long areaCode;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 父级行政代码
     */
    private Long parentCode;

    /**
     * 邮政编码
     */
    private Integer zipCode;

    /**
     * 区号
     */
    private String cityCode;

    /**
     * 名称
     */
    private String name;

    /**
     * 简称
     */
    private String shortName;

    /**
     * 组合名
     */
    private String mergerName;

    /**
     * 拼音
     */
    private String pinyin;

    /**
     * 经度
     */
    private Double lng;

    /**
     * 纬度
     */
    private Double lat;

}
