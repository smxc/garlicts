package com.garlicts.framework.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定接口的实现类
 * 	和Autowired注解配合使用，为接口指定实现类
 *
 * @author 水木星辰
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Impl {

    Class<?> value();
}
