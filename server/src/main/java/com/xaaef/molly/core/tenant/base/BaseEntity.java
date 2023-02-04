package com.xaaef.molly.core.tenant.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
public class BaseEntity implements java.io.Serializable {

    /**
     * 创建时间
     *
     * @date 2019/12/11 21:12
     */
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    /**
     * 创建人 id
     *
     * @date 2019/12/11 21:12
     */
    @TableField(fill = FieldFill.INSERT)
    protected Long createUser;

    /**
     * 最后一次修改时间
     *
     * @date 2019/12/11 21:12
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime lastUpdateTime;

    /**
     * 最后一次修改人 id
     *
     * @date 2019/12/11 21:12
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Long lastUpdateUser;
}
