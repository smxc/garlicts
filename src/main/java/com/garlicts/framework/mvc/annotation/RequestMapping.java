package com.garlicts.framework.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义控制器的请求映射
 *
 * @author 水木星辰
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

	String value();
	
	RequestMethod[] method() default {RequestMethod.GET, RequestMethod.POST};
	
}
