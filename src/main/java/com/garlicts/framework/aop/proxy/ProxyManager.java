package com.garlicts.framework.aop.proxy;

import java.lang.reflect.Method;
import java.util.List;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 代理管理器
 *
 * @author 水木星辰
 * @since 1.0
 */
public class ProxyManager {

	/**
	 * 根据目标类targetClass和代理链proxyList，生成代理对象
	 * @param targetClass
	 * @param proxyList
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyList) {
    	
    	System.out.println("ProxyManager#createProxy===================================");
    	
        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            @Override
            public Object intercept(Object targetObject, Method targetMethod, Object[] methodParams, MethodProxy methodProxy) throws Throwable {
                return new ProxyChain(targetClass, targetObject, targetMethod, methodProxy, methodParams, proxyList).doProxyChain();
            }
        });
    }
}
