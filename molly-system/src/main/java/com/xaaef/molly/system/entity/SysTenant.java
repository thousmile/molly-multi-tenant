package com.xaaef.molly.system.entity;

import com.xaaef.molly.core.tenant.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


/**
 * <p>
 * 租户
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */


@Entity
@Table(name = "sys_tenant")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysTenant extends BaseEntity {

    /**
     * 租户 ID
     */
    @Id
    private String tenantId;

    /**
     * 租户 logo
     */
    @Column(nullable = false)
    private String logo;

    /**
     * 租户名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 租户邮箱
     */
    @Column(nullable = false)
    private String email;

    /**
     * 联系人名称
     */
    @Column(nullable = false)
    private String linkman;

    /**
     * 联系电话
     */
    @Column(nullable = false)
    private String contactNumber;

    /**
     * 行政地址
     */
    @Column(nullable = false)
    private Long areaCode;

    /**
     * 联系地址
     */
    @Column(nullable = false)
    private String address;

    /**
     * 状态 【0.禁用 1.正常】
     */
    @Column(nullable = false)
    private Byte status;

    /**
     * 过期时间  默认是 100 年
     */
    @Column(nullable = false)
    private LocalDateTime expired;

}
