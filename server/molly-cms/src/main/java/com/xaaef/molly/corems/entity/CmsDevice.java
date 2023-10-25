package com.xaaef.molly.corems.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaaef.molly.tenant.base.ProjectBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;


/**
 * <p>
 * 设备表
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2023/10/25 9:31
 */

@Schema(description = "设备")
@TableName("cms_device")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CmsDevice extends ProjectBaseEntity {

    /**
     * 设备 ID
     */
    @Schema(description = "设备ID")
    @TableId(type = IdType.ASSIGN_ID)
    private Long deviceId;

    /**
     * 设备名称
     */
    @Schema(description = "设备名称")
    private String deviceName;

    /**
     * 状态 【0.禁用 1.正常 2.锁定 】
     */
    @Schema(description = "状态 【0.禁用 1.正常 2.锁定 】")
    private Byte status;

}
