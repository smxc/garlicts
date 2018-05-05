package com.garlicts.framework;

import org.slf4j.Logger;

import com.garlicts.framework.aop.AopProxyComponent;
import com.garlicts.framework.dao.DatabaseComponent;
import com.garlicts.framework.ioc.BeanContainerComponent;
import com.garlicts.framework.ioc.IocComponent;
import com.garlicts.framework.mvc.ControllerComponent;
import com.garlicts.framework.plugin.PluginComponent;
import com.garlicts.framework.security.SecurityComponent;
import com.garlicts.framework.util.ClassUtil;

import org.slf4j.LoggerFactory;

/**
 * 组件加载器
 *
 * @author 水木星辰
 * @since 1.0
 */
public final class ComponentLoader {
	
	private static Logger logger = LoggerFactory.getLogger(ComponentLoader.class);

    public static void init() {
        // 定义需要加载的 Helper 类
        Class<?>[] componentList = {
            DatabaseComponent.class,
            ControllerComponent.class,
            BeanContainerComponent.class,
            AopProxyComponent.class,
            PluginComponent.class,
            IocComponent.class,
            SecurityComponent.class
        };
        // 按照顺序加载类
        for (Class<?> cls : componentList) {
            ClassUtil.loadClass(cls.getName());
            logger.info(new StringBuffer("加载框架组件：").append(cls.getName()).toString());
        }
    }
}
