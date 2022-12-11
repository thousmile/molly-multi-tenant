package com.xaaef.molly.system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


/**
 * <p>
 * 中国行政区域
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */


@Entity
@Table(name = "china_area")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChinaArea implements java.io.Serializable {

    /**
     * 行政代码 [ 唯一 ]
     */
    @Id
    private Long areaCode;

    /**
     * 级别
     */
    @Column(nullable = false)
    private Integer level;

    /**
     * 父级行政代码
     */
    @Column(nullable = false)
    private Long parentCode;

    /**
     * 邮政编码
     */
    @Column(nullable = false)
    private Integer zipCode;

    /**
     * 区号
     */
    @Column(nullable = false, unique = true)
    private String cityCode;

    /**
     * 名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 简称
     */
    @Column(nullable = false)
    private String shortName;

    /**
     * 组合名
     */
    @Column(nullable = false)
    private String mergerName;

    /**
     * 拼音
     */
    @Column(nullable = false)
    private String pinyin;

    /**
     * 经度
     */
    @Column(nullable = false)
    private Double lng;

    /**
     * 纬度
     */
    @Column(nullable = false)
    private Double lat;

}
