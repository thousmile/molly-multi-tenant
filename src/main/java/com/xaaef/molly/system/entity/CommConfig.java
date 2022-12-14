package com.xaaef.molly.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaaef.molly.core.tenant.base.BaseEntity;
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


@TableName("comm_config")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CommConfig extends BaseEntity {

    /**
     * 参数主键
     */
    @TableId(type = IdType.AUTO)
    private Long configId;

    /**
     * 参数名称
     */
    private String configName;

    /**
     * 参数键名
     */
    private String configKey;

    /**
     * 参数键值
     */
    private String configValue;

    /**
     * 系统内置（1.是 0.否）
     */
    private Byte configType;

}
