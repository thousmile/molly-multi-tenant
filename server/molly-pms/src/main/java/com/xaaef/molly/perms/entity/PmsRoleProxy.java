package com.xaaef.molly.perms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author WangChenChen
 * @version 1.2
 * @date 2022/6/24 16:33
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PmsRoleProxy implements java.io.Serializable {
    /**
     * 用户id
     */
    @JsonIgnore
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String description;

}
