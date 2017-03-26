package com.garlicts.framework.mvc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.garlicts.framework.FrameworkConstant;
import com.garlicts.framework.InstanceFactory;
import com.garlicts.framework.config.PropertiesProvider;
import com.garlicts.framework.core.BeanLoaderTemplate;
import com.garlicts.framework.mvc.annotation.Controller;
import com.garlicts.framework.mvc.annotation.RequestMapping;
import com.garlicts.framework.mvc.annotation.RequestMethod;
import com.garlicts.framework.util.ArrayUtil;
import com.garlicts.framework.util.CollectionUtil;
import com.garlicts.framework.util.StringUtil;

/**
 * 初始化 Controller 配置
 *
 * @author 水木星辰
 * @since 1.0
 */
public class ControllerAbility {

    /**
     * Action Map（HTTP 请求与 Action 方法的映射）
     */
    private static final Map<Requestor, Handler> controllerMap = new LinkedHashMap<Requestor, Handler>();
    
    /**
     * 获取基础包名
     */
//    private static final String basePackage = PropertiesProvider.getString(FrameworkConstant.BASE_PACKAGE);
    
    /**
     * 获取Bean扫描器
     */
    private static final BeanLoaderTemplate beanLoader = InstanceFactory.getBeanLoaderTemplate();
    
    static {
        // 获取所有 控制器 类（类上标记了Controller注解的类）
        List<Class<?>> controllerClassList = beanLoader.getBeanClassListByAnnotation(Controller.class);
        if (CollectionUtil.isNotEmpty(controllerClassList)) {
            // 定义两个 Action Map
            Map<Requestor, Handler> commonControllerMap = new HashMap<Requestor, Handler>(); // 存放普通 Controller Map
            Map<Requestor, Handler> regexpControllerMap = new HashMap<Requestor, Handler>(); // 存放带有正则表达式的 Controller Map
            // 遍历 Action 类
            for (Class<?> controllerClass : controllerClassList) {
                // 获取并遍历该 Action 类中所有的方法
                Method[] controllerMethods = controllerClass.getDeclaredMethods();
                if (ArrayUtil.isNotEmpty(controllerMethods)) {
                    for (Method controllerMethod : controllerMethods) {
                        // 处理 Action 方法
                        handleActionMethod(controllerClass, controllerMethod, commonControllerMap, regexpControllerMap);
                    }
                }
            }
            // 初始化最终的 Action Map（将 Common 放在 Regexp 前面）
            controllerMap.putAll(commonControllerMap);
            controllerMap.putAll(regexpControllerMap);
        }
    }

    private static void handleActionMethod(Class<?> controllerClass, Method controllerMethod, Map<Requestor, Handler> commonControllerMap, Map<Requestor, Handler> regexpControllerMap) {
    	
    	// 判断当前 Action 方法是否带有 RequestMapping 注解
		if (controllerMethod.isAnnotationPresent(RequestMapping.class)) {
			String requestPath = controllerMethod.getAnnotation(RequestMapping.class).value();
			RequestMethod requestMethod[] = controllerMethod.getAnnotation(RequestMapping.class).method();
			for(RequestMethod e : requestMethod){
				putActionMap(e.name(), requestPath, controllerClass, controllerMethod, commonControllerMap, regexpControllerMap);
			}
		}
    	
    }

    private static void putActionMap(String requestMethod, String requestPath, Class<?> actionClass, Method actionMethod, Map<Requestor, Handler> commonActionMap, Map<Requestor, Handler> regexpActionMap) {
        // 判断 Request Path 中是否带有占位符
        if (requestPath.matches(".+\\{\\w+\\}.*")) {
            // 将请求路径中的占位符 {\w+} 转换为正则表达式 (\\w+)
            requestPath = StringUtil.replaceAll(requestPath, "\\{\\w+\\}", "(\\\\w+)");
            // 将 Requestor 与 Handler 放入 Regexp Action Map 中
            regexpActionMap.put(new Requestor(requestMethod, requestPath), new Handler(actionClass, actionMethod));
        } else {
            // 将 Requestor 与 Handler 放入 Common Action Map 中
            commonActionMap.put(new Requestor(requestMethod, requestPath), new Handler(actionClass, actionMethod));
        }
    }

    /**
     * 获取 Action Map
     */
    public static Map<Requestor, Handler> getControllerMap() {
        return controllerMap;
    }
}
