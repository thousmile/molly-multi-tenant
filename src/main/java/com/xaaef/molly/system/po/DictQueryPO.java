package com.xaaef.molly.system.po;

import com.xaaef.molly.common.po.SearchPO;
import lombok.Data;
import lombok.ToString;


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
public class DictQueryPO extends SearchPO {

    /**
     * 字典类型
     */
    private String dictTypeKey;

}
