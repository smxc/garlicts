package com.garlicts.framework.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义切面类
 *
 * @author 水木星辰
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    /**
     * 切入的包路径
     */
    String packageName() default "";

    /**
     * 切入的具体类名
     */
    String className() default "";

}
