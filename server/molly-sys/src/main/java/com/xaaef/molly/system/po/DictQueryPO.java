package com.xaaef.molly.system.po;

import com.xaaef.molly.common.po.SearchPO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;


/**
 * <p>
 * 字典查询，参数
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 2.0
 * @date 2019/4/18 11:45
 */

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class DictQueryPO extends SearchPO {

    /**
     * 字典类型
     */
    @Schema(description = "字典类型", requiredMode = REQUIRED)
    private String dictTypeKey;

}
