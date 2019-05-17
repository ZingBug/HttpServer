package com.zingbug.httpserver.common.annotation;

import java.lang.annotation.*;

/**
 * Created by ZingBug on 2019/5/14.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Filter {
    String name() default "";
    int order() default 0;//优先级
}
