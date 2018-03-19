package com.garlicts.framework.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * http服务
 *
 * @author 水木星辰
 * @since 1.0
 * 
 * 该注解必须和RequestMapping同时使用
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RemoteHttpService {
	
	String serviceName();
	
}
