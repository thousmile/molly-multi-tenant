package com.xaaef.molly.perms.po;

import com.xaaef.molly.common.po.SearchParentPO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <p>
 * 用户登录成功后，返回的按钮权限
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 2.0
 * @date 2019/4/18 11:45
 */


@Getter
@Setter
@ToString
@Schema(description = "关联菜单")
public class UserQueryPO extends SearchParentPO {

    /**
     * 部门 ID
     */
    private Long deptId;

    /**
     * 包含关联的角色和部门信息
     * include roles and departments
     */
    @Schema(description = "包含关联的角色和部门信息")
    private Boolean includeRad;

    public boolean isIncludeRad() {
        return this.getIncludeRad() != null && this.getIncludeRad();
    }

}
