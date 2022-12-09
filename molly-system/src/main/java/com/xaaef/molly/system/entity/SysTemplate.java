package com.xaaef.molly.system.entity;

import com.xaaef.molly.core.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


/**
 * <p>
 * 租户的 权限模板表
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */

@Entity
@Table(name = "sys_template")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysTemplate extends BaseEntity {

    /**
     * 模板 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 租户 logo
     */
    private String name;

    /**
     * 租户名称
     */
    private String description;

}
