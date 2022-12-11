package com.xaaef.molly.core.tenant.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/9 13:39
 */


@Getter
@Setter
@MappedSuperclass
public class BaseEntity implements java.io.Serializable {

    /**
     * 创建时间
     */
    @Column(updatable = false, nullable = false)
    @CreatedDate
    protected LocalDateTime createTime;

    /**
     * 创建人 id
     */
    @Column(updatable = false, nullable = false)
    @CreatedBy
    protected Long createUser;

    /**
     * 最后一次修改时间
     */
    @LastModifiedDate
    @Column(nullable = false)
    protected LocalDateTime lastUpdateTime;

    /**
     * 最后一次修改人 id
     */
    @LastModifiedBy
    @Column(nullable = false)
    protected Long lastUpdateUser;

}
