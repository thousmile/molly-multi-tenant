package com.xaaef.molly.tenant.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xaaef.molly.internal.dto.OperateUserDTO;
import io.swagger.v3.oas.annotations.media.Schema;
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
     */
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected LocalDateTime createTime;

    /**
     * 创建人 id
     */
    @Schema(description = "创建人 id")
    @TableField(fill = FieldFill.INSERT)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected Long createUser;

    /**
     * 创建人信息
     */
    @Schema(description = "创建人信息")
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected OperateUserDTO createUserEntity;

    /**
     * 最后一次修改时间
     */
    @Schema(description = "最后一次修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected LocalDateTime lastUpdateTime;

    /**
     * 最后一次修改人 id
     */
    @Schema(description = "最后一次修改人 id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected Long lastUpdateUser;

    /**
     * 最后一次修改人信息
     */
    @Schema(description = "最后一次修改人信息")
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected OperateUserDTO lastUpdateUserEntity;

}
