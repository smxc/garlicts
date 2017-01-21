package com.garlicts.aop.proxy;

/**
 * 代理接口
 *
 * @author 水木星辰
 * @since 1.0
 */
public interface Proxy {

    /**
     * 执行链式代理
     *
     * @param proxyChain 代理链
     * @return 目标方法返回值
     * @throws Throwable 异常
     */
    Object doProxy(ProxyChain proxyChain) throws Throwable;
    
}
