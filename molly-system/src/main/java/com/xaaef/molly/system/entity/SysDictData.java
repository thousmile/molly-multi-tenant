package com.xaaef.molly.system.entity;

import com.xaaef.molly.core.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * <p>
 * 字典数据表
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/15 10:42
 */

@Entity
@Table(name = "sys_dict_data")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysDictData extends BaseEntity {

    /**
     * 字典类型 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dictCode;

    /**
     * 字典排序
     */
    @Column(nullable = false)
    private Integer dictSort;

    /**
     * 字典标签
     */
    @Column(nullable = false)
    private String dictLabel;

    /**
     * 字典键值
     */
    @Column(nullable = false)
    private String dictValue;

    /**
     * 字典类型关键字
     */
    @Column(nullable = false)
    private String typeKey;

    /**
     * 是否默认（1.是 0.否）
     */
    @Column(nullable = false)
    private Byte isDefault;

}
