package com.xaaef.molly.common.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * <p>
 * 基础的租户信息
 * </p>
 *
 * @author WangChenChen
 * @version 1.2
 * @date 2022/7/22 10:12
 */


@Schema(description = "租户")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SmallTenant implements java.io.Serializable {

    /**
     * 租户 ID
     */
    @Schema(description = "租户ID")
    private String tenantId;

    /**
     * 租户 logo
     */
    @Schema(description = "租户logo")
    private String logo;

    /**
     * 租户名称
     */
    @Schema(description = "租户名称")
    private String name;

    /**
     * 联系人名称
     */
    @Schema(description = "联系人名称")
    private String linkman;

    /**
     * 项目id列表
     */
    @Schema(description = "项目Id列表")
    private Set<Long> projectIds;

}
