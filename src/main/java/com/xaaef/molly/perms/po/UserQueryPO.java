package com.xaaef.molly.perms.po;

import com.xaaef.molly.common.po.SearchParentPO;
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
public class UserQueryPO extends SearchParentPO {

    /**
     * 部门 ID
     */
    private Long deptId;

}
