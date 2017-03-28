package com.garlicts.framework.ioc;

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
            	
                // 注册Bean
            	registerBean(cls);
                
                // 注册初始化类
            	registerInitializeData(cls);
                
            }
            
            if(!basePackage.contains("com.garlicts.framework.plugin")){
            	
            	List<Class<?>> pluginList = beanLoaderTemplate.getBeanClassList("com.garlicts.framework.plugin");
            	for(Class<?> cls : pluginList){
                    // 注册插件
                	registerPlugin(cls);
            	}
            	
            }
            
            // 注册JdbcTemplate
            registerJdbcTemplate();
            
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
    
    private static void registerJdbcTemplate(){
    	
        //创建JdbcTemplate实例，并将JdbcTemplate注册到Bean容器
        Class<?> jdbcTemplateClass = ClassUtil.loadClass("com.garlicts.framework.dao.JdbcTemplate");
        Object jdbcTemplateInstance;
		try {
			jdbcTemplateInstance = jdbcTemplateClass.newInstance();
			beanMap.put(jdbcTemplateClass, jdbcTemplateInstance);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    }
    
//    private static void registerRedisTemplate(){
//    	
//		//读redis配置  格式为 192.168.8.100
//		String redisServer = PropertiesProvider.getString(FrameworkConstant.REDIS_SERVER);
//		JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), redisServer);
//
//		//将RedisTemplate注册到Bean容器
//		Class<?> redisTemplateClass = ClassUtil.loadClass("com.garlicts.framework.plugin.cache.redis.RedisTemplate");
//		try {
//			Constructor<?> constructor = redisTemplateClass.getConstructor(JedisPool.class);
//			RedisTemplate redisTemplate = (RedisTemplate) constructor.newInstance(jedisPool);
//			BeanContainerAbility.setBean(redisTemplateClass, redisTemplate);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	    
//		
//    	
//    }
    
    /**
     * <p>注册Bean</p>
     * 扫描带有Bean/Service/Controller/Aspect注解的类，并注册到Bean容器
     */
    private static void registerBean(Class<?> cls){
    	
        if (cls.isAnnotationPresent(Bean.class) || 
                cls.isAnnotationPresent(Service.class) || 
                cls.isAnnotationPresent(Controller.class) || 
                cls.isAnnotationPresent(Aspect.class)) {
                // 创建 Bean 实例
                Object beanInstance;
				try {
					
					beanInstance = cls.newInstance();
					
	                // 将 Bean 实例放入 Bean Map 中（键为 Bean 类，值为 Bean 实例）
	                beanMap.put(cls, beanInstance);
	                //打印注册的bean
	                logger.info(new StringBuffer("注册Bean[Class | Class的对象]：").append(cls).append(" | ").append(beanInstance).toString());
	                
				} catch (Exception e) {
					e.printStackTrace();
				}

            }
    	
    }
    
    /**
     * <p>注册插件</p>
     * 插件都实现Plugin接口 
     */
    public static void registerPlugin(Class<?> cls){
    	
        if(Plugin.class.isAssignableFrom(cls) && !cls.equals(Plugin.class)){
        	Object beanInstance;
			try {
				beanInstance = cls.newInstance();
	        	beanMap.put(cls, beanInstance);
	        	logger.info(new StringBuffer("注册插件：").append(cls.getName()).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}

        }    	
    	
    }
    
    /**
     * 注册初始化类 
     * 初始化类都实现InitializeData接口
     */
    public static void registerInitializeData(Class<?> cls){
    	
        if(InitializeData.class.isAssignableFrom(cls) && !cls.equals(InitializeData.class)){
        	Object initInstance;
			try {
				initInstance = cls.newInstance();
	        	beanMap.put(cls, initInstance);
	        	logger.info(new StringBuffer("注册初始化类：").append(cls.getName()).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}

        }
        
    }
    
}
