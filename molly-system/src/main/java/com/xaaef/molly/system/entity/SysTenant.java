package com.xaaef.molly.system.entity;

import com.xaaef.molly.core.base.BaseEntity;
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
    private String logo;

    /**
     * 租户名称
     */
    private String name;

    /**
     * 租户邮箱
     */
    private String email;

    /**
     * 联系人名称
     */
    private String linkman;

    /**
     * 联系电话
     */
    private String contactNumber;

    /**
     * 行政地址
     */
    private Long areaCode;

    /**
     * 联系地址
     */
    private String address;

    /**
     * 状态 【0.禁用 1.正常】
     */
    private Byte status;

    /**
     * 过期时间  默认是 100 年
     */
    private LocalDateTime expired;

}
