package com.zingbug.httpserver.common.annotation;

import java.lang.annotation.*;

/**
 * Created by ZingBug on 2019/5/17.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Controller {
    String url() default "";
}
