package com.xaaef.molly.web.repeat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO 防止重复提交 注解
 *
 * @author WangChenChen
 * @date 2023/10/31 19:30
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoRepeatSubmit {

    /**
     * 默认失效时间 1500 毫秒
     */
    long value() default 1500;

}
