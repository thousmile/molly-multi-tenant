package com.xaaef.molly.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaaef.molly.common.valid.ValidCreate;
import com.xaaef.molly.common.valid.ValidUpdate;
import com.xaaef.molly.tenant.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 通用父实体类
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */


@Schema(description = "全局配置")
@TableName("sys_config")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysConfig extends BaseEntity {

    /**
     * 参数主键
     */
    @Schema(description = "配置ID")
    @TableId(type = IdType.AUTO)
    @NotNull(message = "配置ID,必须填写", groups = {ValidUpdate.class})
    private Long configId;

    /**
     * 参数名称
     */
    @Schema(description = "参数名称")
    @NotEmpty(message = "参数名称,必须填写", groups = {ValidCreate.class})
    private String configName;

    /**
     * 参数键名
     */
    @Schema(description = "参数键名")
    @NotEmpty(message = "参数键名,必须填写", groups = {ValidCreate.class})
    private String configKey;

    /**
     * 参数键值
     */
    @Schema(description = "参数键值")
    @NotEmpty(message = "参数键值,必须填写", groups = {ValidCreate.class})
    private String configValue;

    /**
     * 系统内置（1.是 0.否）
     */
    @Schema(description = "系统内置（1.是 0.否）")
    @NotNull(message = "系统内置,必须填写", groups = {ValidCreate.class})
    private Byte configType;

}
