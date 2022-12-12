package com.xaaef.molly.system.entity;

import com.xaaef.molly.core.tenant.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * <p>
 * 通用父实体类
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */


@Entity
@Table(name = "comm_config")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysConfig extends BaseEntity {

    /**
     * 参数主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long configId;

    /**
     * 参数名称
     */
    @Column(nullable = false)
    private String configName;

    /**
     * 参数键名
     */
    @Column(nullable = false, unique = true)
    private String configKey;

    /**
     * 参数键值
     */
    @Column(nullable = false)
    private String configValue;

    /**
     * 系统内置（1.是 0.否）
     */
    @Column(nullable = false)
    private Byte configType;

}
