package com.garlicts.aop;

import java.lang.reflect.Method;

import com.garlicts.aop.proxy.Proxy;
import com.garlicts.aop.proxy.ProxyChain;

/**
 * 切面代理
 *
 * @author 水木星辰
 * @since 1.0
 */
public abstract class AspectProxy implements Proxy {

    @Override
    public final Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;

        Class<?> targetClass = proxyChain.getTargetClass();
        Method targetMethod = proxyChain.getTargetMethod();
        Object[] methodParams = proxyChain.getMethodParams();

        before(targetClass, targetMethod, methodParams);
        result = proxyChain.doProxyChain();
        after(targetClass, targetMethod, methodParams, result);

        return result;
    }

    public abstract void before(Class<?> targetClass, Method targetMethod, Object[] methodParams);

    public abstract void after(Class<?> targetClass, Method targetMethod, Object[] methodParams, Object result);

}
