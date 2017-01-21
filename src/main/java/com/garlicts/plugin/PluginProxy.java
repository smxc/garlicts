package com.garlicts.plugin;

import java.util.List;

import com.garlicts.aop.proxy.Proxy;

/**
 * 插件代理
 *
 * @author 水木星辰
 * @since 1.0
 */
public abstract class PluginProxy implements Proxy {

    public abstract List<Class<?>> getTargetClassList();
}
