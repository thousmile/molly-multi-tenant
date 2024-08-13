package com.xaaef.molly.common.consts;


/**
 * <p>
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2024/8/13 15:15
 */

public class DataScopeConst {

    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：仅本部门数据权限 4：本部门及以下数据权限）
     */
    public static final Integer All = 1;

    public static final Integer CUSTOM = 2;

    public static final Integer ONLY_ME = 3;

    public static final Integer ME_AND_CHILD = 4;

}
