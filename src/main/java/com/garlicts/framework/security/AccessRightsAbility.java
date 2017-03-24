package com.garlicts.framework.security;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.garlicts.framework.FrameworkConstant;
import com.garlicts.framework.InstanceFactory;
import com.garlicts.framework.config.PropertiesProvider;
import com.garlicts.framework.core.BeanLoaderTemplate;
import com.garlicts.framework.core.fault.InitializationError;
import com.garlicts.framework.mvc.annotation.Controller;
import com.garlicts.framework.security.annotation.Role;
import com.garlicts.framework.util.StringUtil;

/**
 * 权限访问能力
 * 在控制器的方法上加上@Role("roleName")注解，表示只有"roleName"的角色才可以访问该服务。@Role 用于控制器的方法
 * @since 1.0
 * @author 水木星辰
 */
public class AccessRightsAbility {

	private static final Logger logger = LoggerFactory.getLogger(AccessRightsAbility.class);

	private static final BeanLoaderTemplate beanLoaderTemplate = InstanceFactory.getBeanLoaderTemplate();
	
    /**
     * accessRightsMap用来存储“控制器方法名称”和“角色名”的对应关系
     */
    private static Map<String, String> accessRightsMap = new ConcurrentHashMap<String, String>();
    
    /**
     * 获取基础包名
     */
    private static final String basePackage = PropertiesProvider.getString(FrameworkConstant.BASE_PACKAGE);
    
    static {
        try {
            // 获取应用包路径下所有的类
        	List<Class<?>> classList = beanLoaderTemplate.getBeanClassListByAnnotation(basePackage, Controller.class);
        	String key = null;
        	
            for (Class<?> cls : classList) {
            	
            	Method[] methods = cls.getMethods();
            	for(Method method : methods){
            		if(!method.isAccessible()){
            			method.setAccessible(true);
            		}
            		
            		if(method.isAnnotationPresent(Role.class)){
                		Role roleAnnotation = method.getAnnotation(Role.class);
                		String roleName = roleAnnotation.value();
                		if(StringUtil.isNotBlank(roleName)){
                			key = cls.getName() + "." + method.getName();
                			setBean(key, roleName);
                			logger.info(new StringBuffer("加载权限配置：").append(key).append(" : ").append(roleName).toString());
                		}
            		}

            	}
                
            }
            
        } catch (Exception e) {
            throw new InitializationError("初始化  BeanContainerAbility 出错！");
        }
    }

    /**
     * 获取accessRightsMap
     */
    public static Map<String, String> getAccessRightsMap() {
        return accessRightsMap;
    }
    
    /**
     * 获取角色名
     */
    public static String getAccessRights(String methodName) {
        if (!accessRightsMap.containsKey(methodName)) {
            throw new RuntimeException("找不到权限配置信息");
        }
        return accessRightsMap.get(methodName);
    }

    /**
     * 保存控制器的方法名和角色名的对应关系
     */
    public static void setBean(String methodName, String accessRights) {
    	accessRightsMap.put(methodName, accessRights);
    }
    
    /**
     * 销毁容器的所有数据 
     */
    public static void destroy(){
    	accessRightsMap.clear();
    }	
    
}
