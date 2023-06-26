package com.wwk.annotation;

import org.springframework.stereotype.Repository;

import java.lang.annotation.*;

/**
 * 访问日志注解
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface VisitLogger {
    /**
     *
     * @return 访问了什么页面
     */
    String value() default "";
}
