package com.xaaef.molly.corems.po;

import com.xaaef.molly.common.po.SearchParentPO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <p>
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 2.0
 * @date 2019/4/18 11:45
 */


@Getter
@Setter
@ToString
@Schema(description = "项目查询")
public class ProjectQueryPO extends SearchParentPO {

    /**
     * 部门 ID
     */
    @Schema(description = "部门ID")
    private Long deptId;

    /**
     * 包含关联部门信息
     * include departments
     */
    @Schema(description = "包含关联部门信息")
    private Boolean includeDept;

    public boolean isIncludeDept() {
        return this.getIncludeDept() != null && this.getIncludeDept();
    }

}
