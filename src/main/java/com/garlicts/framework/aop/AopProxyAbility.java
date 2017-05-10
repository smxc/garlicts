package com.garlicts.framework.aop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.garlicts.framework.InstanceFactory;
import com.garlicts.framework.aop.annotation.Aspect;
import com.garlicts.framework.aop.proxy.Proxy;
import com.garlicts.framework.aop.proxy.ProxyManager;
import com.garlicts.framework.core.BeanLoaderTemplate;
import com.garlicts.framework.core.fault.InitializationError;
import com.garlicts.framework.ioc.BeanContainerAbility;
import com.garlicts.framework.transaction.TransactionProxy;
import com.garlicts.framework.transaction.annotation.Service;
import com.garlicts.framework.util.ClassUtil;
import com.garlicts.framework.util.StringUtil;

/**
 * 初始化 AOP 框架
 *
 * @author 水木星辰
 * @since 1.0
 */
public class AopProxyAbility {

    /**
     * 获取Bean扫描器
     */
    private static final BeanLoaderTemplate beanLoader = InstanceFactory.getBeanLoaderTemplate();

    static {
        try {
            // 创建 Proxy Map，存放【代理类】与【委托类List】的映射关系
            Map<Class<?>, List<Class<?>>> proxyMap = createProxyMap();
            // 创建 Target Map，存放【委托类】与【代理类List】的映射关系）
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            // 遍历 Target Map
            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
                Class<?> targetClass = targetEntry.getKey();
                List<Proxy> proxyList = targetEntry.getValue();
                // 创建代理实例
                Object proxyInstance = ProxyManager.createProxy(targetClass, proxyList);
                // 用【代理类实例】覆盖【委托类实例】，并放入 Bean 容器中
                BeanContainerAbility.setBean(targetClass, proxyInstance);
            }
        } catch (Exception e) {
            throw new InitializationError("初始化 AopProxyAbility 出错！");
        }
    }

    private static Map<Class<?>, List<Class<?>>> createProxyMap() throws Exception {
        Map<Class<?>, List<Class<?>>> proxyMap = new LinkedHashMap<Class<?>, List<Class<?>>>();
        // 添加相关代理
//        addPluginProxy(proxyMap);      // 插件代理
        addAspectProxy(proxyMap);      // 切面代理
        addTransactionProxy(proxyMap); // 事务代理
        return proxyMap;
    }

//    private static void addPluginProxy(Map<Class<?>, List<Class<?>>> proxyMap) throws Exception {
//        // 获取插件包名下父类为 PluginProxy 的所有类（插件代理类）
//    	if(StringUtil.isNotEmpty(pluginPackage)){
//            List<Class<?>> pluginProxyClassList = beanLoader.getBeanClassListBySuper(pluginPackage, PluginProxy.class);
//            if (CollectionUtil.isNotEmpty(pluginProxyClassList)) {
//                // 遍历所有插件代理类
//                for (Class<?> pluginProxyClass : pluginProxyClassList) {
//                    // 创建插件代理类实例
//                    PluginProxy pluginProxy = (PluginProxy) pluginProxyClass.newInstance();
//                    // 将插件代理类及其所对应的目标类列表放入 Proxy Map 中
//                    proxyMap.put(pluginProxyClass, pluginProxy.getTargetClassList());
//                }
//            }
//    	}
//
//    }

    private static void addAspectProxy(Map<Class<?>, List<Class<?>>> proxyMap) throws Exception {
    	
        // 获取切面类（所有继承于 AspectProxy 的类）
        List<Class<?>> aspectProxyClassList = beanLoader.getBeanClassListBySuper(AspectProxy.class);
        
        // 遍历切面类
        for (Class<?> aspectProxyClass : aspectProxyClassList) {
            // 判断Aspect注解是否存在
            if (aspectProxyClass.isAnnotationPresent(Aspect.class)) {
                // 获取Aspect注解
                Aspect aspect = aspectProxyClass.getAnnotation(Aspect.class);
                // 创建委托类List（Aspect要切入的委托类）
                List<Class<?>> targetClassList = createTargetClassList(aspect);
                // 初始化 Proxy Map
                proxyMap.put(aspectProxyClass, targetClassList);
            }
        }
    }

    private static void addTransactionProxy(Map<Class<?>, List<Class<?>>> proxyMap) {
        // 使用 TransactionProxy 代理所有 Service 类
        List<Class<?>> serviceClassList = beanLoader.getBeanClassListByAnnotation(Service.class);
        if(serviceClassList.size() > 0){
        	proxyMap.put(TransactionProxy.class, serviceClassList);
        }
        
    }

    /**
     * 创建Aspect注解要切入的委托类List 
     */
    private static List<Class<?>> createTargetClassList(Aspect aspect) throws Exception {
        List<Class<?>> targetClassList = new ArrayList<Class<?>>();
        // 获取 Aspect 注解的相关属性
        String packageName = aspect.packageName();
        String className = aspect.className();
//        Class<? extends Annotation> annotation = aspect.annotation();
        // 若包名不为空，则需进一步判断类名是否为空
        if (StringUtil.isNotEmpty(packageName)) {
            if (StringUtil.isNotEmpty(className)) {
                // 若类名不为空，则仅添加该类
                targetClassList.add(ClassUtil.loadClass(packageName + "." + className, false));
            } else {
            	
            	// 添加该包名下所有类
            	targetClassList.addAll(beanLoader.getBeanClassList(packageName));
            	
            }
        } else {
        	
//        	targetClassList.addAll(beanLoader.getBeanClassListByAnnotation(Aspect.class));
        	throw new InitializationError("切面类的packageName不能为空。");
        	
        }
        return targetClassList;
    }

    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, List<Class<?>>> proxyMap) throws Exception {
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
        // 遍历 Proxy Map
        for (Map.Entry<Class<?>, List<Class<?>>> proxyEntry : proxyMap.entrySet()) {
            // 分别获取 map 中的 key 与 value
            Class<?> proxyClass = proxyEntry.getKey();
            List<Class<?>> targetClassList = proxyEntry.getValue();
            // 遍历目标类列表
            for (Class<?> targetClass : targetClassList) {
                // 创建代理类（切面类）实例
                Proxy baseAspect = (Proxy) proxyClass.newInstance();
                // 初始化 Target Map
                if (targetMap.containsKey(targetClass)) {
                    targetMap.get(targetClass).add(baseAspect);
                } else {
                    List<Proxy> baseAspectList = new ArrayList<Proxy>();
                    baseAspectList.add(baseAspect);
                    targetMap.put(targetClass, baseAspectList);
                }
            }
        }
        return targetMap;
    }
}
