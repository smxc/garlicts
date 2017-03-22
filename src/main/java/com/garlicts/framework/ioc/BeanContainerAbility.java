package com.garlicts.framework.ioc;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.garlicts.framework.FrameworkConstant;
import com.garlicts.framework.InitializeData;
import com.garlicts.framework.InstanceFactory;
import com.garlicts.framework.aop.annotation.Aspect;
import com.garlicts.framework.config.PropertiesProvider;
import com.garlicts.framework.core.BeanLoaderTemplate;
import com.garlicts.framework.core.fault.InitializationError;
import com.garlicts.framework.ioc.annotation.Bean;
import com.garlicts.framework.mvc.annotation.Controller;
import com.garlicts.framework.plugin.Plugin;
import com.garlicts.framework.transaction.annotation.Service;
import com.garlicts.framework.util.ClassUtil;

/**
 * 初始化相关 Bean 类
 *
 * @author 水木星辰
 * @since 1.0
 */
public class BeanContainerAbility{
	
	private static final Logger logger = LoggerFactory.getLogger(BeanContainerAbility.class);

	private static final BeanLoaderTemplate beanLoaderTemplate = InstanceFactory.getBeanLoaderTemplate();
	
    /**
     * Bean Map（Bean的Class对应实例）
     */
    private static Map<Class<?>, Object> beanMap = new ConcurrentHashMap<Class<?>, Object>();
    
    /**
     * 获取基础包名
     */
    private static final String basePackage = PropertiesProvider.getString(FrameworkConstant.BASE_PACKAGE);
    
    static {
        try {
            // 获取应用包路径下所有的类
        	List<Class<?>> classList = beanLoaderTemplate.getBeanClassList(basePackage);
        	
            for (Class<?> cls : classList) {
            	
                // 处理带有 Bean/Service/Controller/Aspect 注解的类
                if (cls.isAnnotationPresent(Bean.class) || 
                    cls.isAnnotationPresent(Service.class) || 
                    cls.isAnnotationPresent(Controller.class) || 
                    cls.isAnnotationPresent(Aspect.class)) {
                    // 创建 Bean 实例
                    Object beanInstance = cls.newInstance();
                    // 将 Bean 实例放入 Bean Map 中（键为 Bean 类，值为 Bean 实例）
                    beanMap.put(cls, beanInstance);
                    
                    //打印注册的bean
                    logger.info(new StringBuffer("加载Class[Class | Class的对象]：").append(cls).append(" | ").append(beanInstance).toString());
                    
                }
                
                //加载插件
                if(Plugin.class.isAssignableFrom(cls) && !cls.equals(Plugin.class)){
                	Object beanInstance = cls.newInstance();
                	beanMap.put(cls, beanInstance);
                	logger.info(new StringBuffer("加载插件：").append(cls.getName()).toString());
                }
                
                //加载初始化类
                if(InitializeData.class.isAssignableFrom(cls) && !cls.equals(InitializeData.class)){
                	Object initInstance = cls.newInstance();
                	beanMap.put(cls, initInstance);
                	logger.info(new StringBuffer("加载初始化类：").append(cls.getName()).toString());
                }
                
            }
            
            //创建JdbcTemplate实例，并将JdbcTemplate注册到Bean容器
            Class<?> jdbcTemplateClass = ClassUtil.loadClass("com.garlicts.framework.dao.JdbcTemplate");
            Object jdbcTemplateInstance = jdbcTemplateClass.newInstance();
            beanMap.put(jdbcTemplateClass, jdbcTemplateInstance);
            
        } catch (Exception e) {
            throw new InitializationError("初始化  BeanContainerAbility 出错！");
        }
    }

    /**
     * 获取 Bean Map
     */
    public static Map<Class<?>, Object> getBeanMap() {
        return beanMap;
    }
    
    /**
     * 获取 Bean 实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> cls) {
        if (!beanMap.containsKey(cls)) {
            throw new RuntimeException("无法根据类名获取实例！" + cls);
        }
        return (T) beanMap.get(cls);
    }

    /**
     * 设置 Bean 实例
     */
    public static void setBean(Class<?> cls, Object obj) {
        beanMap.put(cls, obj);
    }
    
    /**
     * 销毁容器的所有数据 
     */
    public static void destroy(){
    	beanMap.clear();
    }
    
}
