package com.xaaef.molly.tenant.ds;

import java.lang.annotation.*;

/**
 * 数据权限过滤注解
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /**
     * 部门的别名
     */
    String deptAlias() default "d";

    /**
     * 用户表的别名
     */
    String userAlias() default "u";

}