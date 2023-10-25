package com.xaaef.molly.tenant.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 项目基础类
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/9 13:39
 */

@Getter
@Setter
@Accessors(chain = true)
public class ProjectBaseEntity extends BaseEntity implements java.io.Serializable {

    /**
     * 项目 id
     *
     * @date 2023/10/10 21:12
     */
    @JsonIgnore
    @Schema(description = "项目ID")
    @TableField(fill = FieldFill.INSERT)
    private Long projectId;

}
