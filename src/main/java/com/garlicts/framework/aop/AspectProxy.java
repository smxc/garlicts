package com.garlicts.framework.aop;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.garlicts.framework.aop.proxy.Proxy;
import com.garlicts.framework.aop.proxy.ProxyChain;

/**
 * 切面代理
 *
 * @author 水木星辰
 * @since 1.0
 */
public abstract class AspectProxy implements Proxy {
	
	Logger logger = LoggerFactory.getLogger(AspectProxy.class);

    @Override
    public final Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;

        Class<?> targetClass = proxyChain.getTargetClass();
        Method targetMethod = proxyChain.getTargetMethod();
        Object[] methodParams = proxyChain.getMethodParams();

        boolean isTrue = before(targetClass, targetMethod, methodParams);
        if(isTrue){
        	result = proxyChain.doProxyChain();
        }else{
        	StringBuffer stringBuffer = new StringBuffer("切面[").append(targetClass).append("#").append(targetMethod.getName()).append("]被AOP组件拦截，原因是：befor方法返回值为false。");
        	logger.warn(stringBuffer.toString());
        }
        after(targetClass, targetMethod, methodParams, result);
        return result;
    }

    public abstract boolean before(Class<?> targetClass, Method targetMethod, Object[] methodParams);

    public abstract boolean after(Class<?> targetClass, Method targetMethod, Object[] methodParams, Object result);

}
