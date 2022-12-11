package com.xaaef.molly.system.entity;

import com.xaaef.molly.core.tenant.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * <p>
 * 字典类型表
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/15 10:42
 */

@Entity
@Table(name = "sys_dict_type")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysDictType extends BaseEntity {

    /**
     * 字典类型 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeId;

    /**
     * 字典类型名
     */
    @Column(nullable = false)
    private String typeName;

    /**
     * 字典类型关键字
     */
    @Column(unique = true, nullable = false)
    private String typeKey;

    /**
     * 字典描述
     */
    @Column(nullable = false)
    private String description;

}
