package com.xaaef.molly.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * <p>
 * </p>
 *
 * @author Wang Chen Chen<932560435@qq.com>
 * @version 1.0.1
 * @date 2021/7/24 14:39
 */

@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "系统用户授权多个租户")
public class UserListTenantVO implements java.io.Serializable {

    /**
     * 已经拥有的 租户ID
     */
    @Schema(description = "拥有的 租户ID！")
    private Set<String> have;

    /**
     * 全部 租户信息
     */
    @Schema(description = "全部 租户信息！")
    private Set<Entry> all;


    @Getter
    @Setter
    @ToString
    @Builder
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Entry implements java.io.Serializable {

        private String value;

        private String label;

    }


}


