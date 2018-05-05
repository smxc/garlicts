package com.garlicts.framework.ioc;

import java.lang.reflect.Field;
import java.util.Map;

import com.garlicts.framework.core.fault.InitializationError;
import com.garlicts.framework.ioc.annotation.Autowired;
import com.garlicts.framework.util.ArrayUtil;

/**
 * 初始化 IOC 容器
 *
 * @author 水木星辰
 * @since 1.0
 */
public class IocComponent {

    static {
        try {
            // 获取并遍历所有的 Bean 类
            Map<Class<?>, Object> beanMap = BeanContainerComponent.getBeanMap();
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                // 获取 Bean 类与 Bean 实例
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                // 获取 Bean 类中所有的字段（不包括父类中的方法）
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(beanFields)) {
                    // 遍历所有的 Bean 字段
                    for (Field beanField : beanFields) {
                        // 判断当前 Bean 字段是否带有 Autowired 注解
                        if (beanField.isAnnotationPresent(Autowired.class)) {
                        
        					Class<?> autowiredClass = beanField.getType();
                            // 从 Bean Map 中获取该实现类对应的类实例
                            Object autowiredInstance = beanMap.get(autowiredClass); 
                            if(autowiredInstance != null){
                            	beanField.setAccessible(true);
                            	beanField.set(beanInstance, autowiredInstance);
                            }else{
                            	throw new InitializationError("依赖注入失败！类名：" + beanClass.getName() + "，字段名：" + autowiredClass.getSimpleName());
                            }
                        	
//                            // 获取 Bean 字段对应的接口
//                            Class<?> interfaceClass = beanField.getType();
//                            // 获取 Bean 字段对应的实现类
//                            Class<?> implementClass = findImplementClass(interfaceClass);
//                            // 若存在实现类，则执行以下代码
//                            if (implementClass != null) {
//                                // 从 Bean Map 中获取该实现类对应的实现类实例
//                                Object implementInstance = beanMap.get(implementClass);
//                                // 设置该 Bean 字段的值
//                                if (implementInstance != null) {
//                                    beanField.setAccessible(true); // 将字段设置为 public
//                                    beanField.set(beanInstance, implementInstance); // 设置字段初始值
//                                } else {
//                                    throw new InitializationError("依赖注入失败！类名：" + beanClass.getSimpleName() + "，字段名：" + interfaceClass.getSimpleName());
//                                }
//                            }
                            
                        }
                        
                    }
                }
            }
        } catch (Exception e) {
            throw new InitializationError("初始化 IocAbility 出错！");
        }
    }

    /**
     * 查找实现类
     */
//    public static Class<?> findImplementClass(Class<?> interfaceClass) {
//        Class<?> implementClass = interfaceClass;
//        // 判断接口上是否标注了 Impl 注解
//        if (interfaceClass.isAnnotationPresent(Impl.class)) {
//            // 获取强制指定的实现类
//            implementClass = interfaceClass.getAnnotation(Impl.class).value();
//        } else {
//            // 获取该接口所有的实现类
//            List<Class<?>> implementClassList = AnnotationBeanFactory.getBeanListBySuper(interfaceClass);
//            if (CollectionUtil.isNotEmpty(implementClassList)) {
//                // 获取第一个实现类
//                implementClass = implementClassList.get(0);
//            }
//        }
//        // 返回实现类对象
//        return implementClass;
//    }
    
}
