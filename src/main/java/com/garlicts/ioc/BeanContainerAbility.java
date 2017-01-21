package com.garlicts.ioc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.garlicts.FrameworkConstant;
import com.garlicts.aop.annotation.Aspect;
import com.garlicts.config.PropertiesProvider;
import com.garlicts.core.BeanLoaderTemplate;
import com.garlicts.core.fault.InitializationError;
import com.garlicts.ioc.annotation.Bean;
import com.garlicts.mvc.annotation.Controller;
import com.garlicts.plugin.DistributedPlugin;
import com.garlicts.transaction.annotation.Service;
import com.garlicts.util.ClassUtil;

/**
 * 初始化相关 Bean 类
 *
 * @author 水木星辰
 * @since 1.0
 */
public class BeanContainerAbility{

    /**
     * Bean Map（Bean的Class对应实例）
     */
    private static Map<Class<?>, Object> beanMap = new HashMap<Class<?>, Object>();
    private static List<Class<?>> beanList = new ArrayList<Class<?>>();
    
    /**
     * 获取基础包名
     */
    private static final String basePackage = PropertiesProvider.getString(FrameworkConstant.BASE_PACKAGE);
    private static final String pluginPackage = PropertiesProvider.getString(FrameworkConstant.PLUGIN_PACKAGE);
    
    static {
        try {
            // 获取应用包路径下所有的类
        	List<Class<?>> classList = new BeanLoaderTemplate().getBeanClassList(basePackage);
        	// 获取插件包路径下所有的类
        	List<Class<?>> pluginClassList = new BeanLoaderTemplate().getBeanClassList(pluginPackage);
        	
            for (Class<?> cls : classList) {
            	
            	if(!beanList.contains(cls)){
            		beanList.add(cls);	
            	}
            	
                // 处理带有 Bean/Service/Controller/Aspect 注解的类
                if (cls.isAnnotationPresent(Bean.class) ||
                    cls.isAnnotationPresent(Service.class) ||
                    cls.isAnnotationPresent(Controller.class) ||
                    cls.isAnnotationPresent(Aspect.class)) {
                    // 创建 Bean 实例
                    Object beanInstance = cls.newInstance();
                    // 将 Bean 实例放入 Bean Map 中（键为 Bean 类，值为 Bean 实例）
                    beanMap.put(cls, beanInstance);
                }
            }
            
            for(Class<?> cls : pluginClassList){
            	
            	if(DistributedPlugin.class.isAssignableFrom(cls) && cls != DistributedPlugin.class){
            		if(!beanList.contains(cls)){
            			beanList.add(cls);
            		}
            	}
            	
            }
            
            //创建JdbcTemplate实例，并将JdbcTemplate注册到Bean容器
            Class<?> jdbcTemplateClass = ClassUtil.loadClass("com.garlicts.dao.JdbcTemplate");
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
    
    public static List<Class<?>> getBeanList(){
    	return beanList;
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
    
   
    
}
