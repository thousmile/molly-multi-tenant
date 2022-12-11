package com.xaaef.molly.system.entity;

import com.xaaef.molly.core.tenant.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 系统通知
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */

@Entity
@Table(name = "sys_notice")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysNotice extends BaseEntity {

    /**
     * 部门 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 部门 名称
     */
    @Column(nullable = false)
    private String title;

    /**
     * 分类 ID
     */
    @Column(nullable = false)
    private Long categoryId;

    /**
     * 发布时间
     */
    @Column(nullable = false)
    private LocalDateTime releaseTime;

    /**
     * 内容
     */
    @Column(nullable = false)
    private String content;

    /**
     * 谁可以查看此公告 [ 0.此商户下的用户  1.全部的用户 ]
     */
    @Column(nullable = false)
    private Byte viewRange;

    /**
     * 状态 【0.已经过期 1.正常 】
     */
    @Column(nullable = false)
    private Byte status;

    /**
     * 过期时间
     */
    @Column(nullable = false)
    private LocalDateTime expired;

}
