package com.garlicts.core;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Bean加载器
 *
 * @author 水木星辰
 * @since 1.0
 */
public interface BeanLoader {

    /**
     * 获取指定包名中的所有类
     */
    List<Class<?>> getBeanClassList(String packageName);

    /**
     * 获取指定包名中指定注解的相关类
     */
    List<Class<?>> getBeanClassListByAnnotation(String packageName, Class<? extends Annotation> annotationClass);

    /**
     * 获取指定包名中指定父类或接口的相关类
     */
    List<Class<?>> getBeanClassListBySuper(String packageName, Class<?> superClass);
    
}
