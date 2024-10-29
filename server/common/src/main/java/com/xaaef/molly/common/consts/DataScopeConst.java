package com.xaaef.molly.common.consts;

/**
 * <p>
 *
 * </p>
 *
 * @author WangChenChen
 * @version 1.0.0
 * @date 2024/10/28 下午6:37
 */


public class DataScopeConst {

    /**
     * 全部数据权限
     */
    public static final int DATA_SCOPE_ALL = 1;

    /**
     * 自定数据权限
     */
    public static final int DATA_SCOPE_CUSTOM = 2;

    /**
     * 部门数据权限
     */
    public static final int DATA_SCOPE_DEPT = 3;

    /**
     * 部门及以下数据权限
     */
    public static final int DATA_SCOPE_DEPT_AND_CHILD = 4;

    /**
     * 仅本人数据权限
     */
    public static final int DATA_SCOPE_SELF = 5;

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";


}
