package com.garlicts.framework.aop.proxy;

import java.lang.reflect.Method;
import java.util.List;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 代理链<br>
 * 链式代理就是将多个代理串链在一起，一个一个的去执行，执行的顺序取决于链上的先后顺序。如果增强有多个，那么就是链式动态代理了。
 * 就像一个请求被多个Filter过滤一样，道理是类似的。<br>
 * <hr>
 * 示意：<br>
 * a.before();
 * b.before();
 * 执行被代理的方法
 * b.after();
 * a.after();
 * <hr>
 *
 * @author 水木星辰
 * @since 1.0
 */
public class ProxyChain {

    private final Class<?> targetClass; // 被代理类
    private final Object targetObject; // 被代理对象
    private final Method targetMethod; // 被代理方法
    private final MethodProxy methodProxy; // 被代理方法的Proxy cglib提供
    private final Object[] methodParams; // 方法参数
    private List<Proxy> proxyList; // 代理链
    private int proxyIndex = 0; // 指向代理链的当前执行位置

    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxyList = proxyList;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    /**
     * 执行代理 
     */
    public Object doProxyChain() throws Throwable {
    	
        Object methodResult;
        
        /**
         * 对代理链进行循环取出代理类，并执行代理类，例如：在被代理类执行之前，先执行代理类的before()增强方法。
         * 当代理类的增强方法执行结束后，最后执行被代理类的业务逻辑。
         * 
         */
        if (proxyIndex < proxyList.size()) {
            methodResult = proxyList.get(proxyIndex++).doProxy(this);
        } else {
        	// 执行被代理类的方法
            methodResult = methodProxy.invokeSuper(targetObject, methodParams);
        }
        return methodResult;
    }
    
}
