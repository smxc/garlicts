package com.garlicts.framework;

import org.slf4j.Logger;

import com.garlicts.framework.aop.AopProxyAbility;
import com.garlicts.framework.dao.DatabaseAbility;
import com.garlicts.framework.ioc.BeanContainerAbility;
import com.garlicts.framework.ioc.IocAbility;
import com.garlicts.framework.mvc.ControllerAbility;
import com.garlicts.framework.plugin.PluginAbility;
import com.garlicts.framework.security.AccessRightsAbility;
import com.garlicts.framework.util.ClassUtil;

import org.slf4j.LoggerFactory;

/**
 * 能力加载
 *
 * @author 水木星辰
 * @since 1.0
 */
public final class AbilityLoader {
	
	private static Logger logger = LoggerFactory.getLogger(AbilityLoader.class);

    public static void init() {
        // 定义需要加载的 Helper 类
        Class<?>[] abilityList = {
            DatabaseAbility.class,
            ControllerAbility.class,
            BeanContainerAbility.class,
            AopProxyAbility.class,
            PluginAbility.class,
            IocAbility.class,
            AccessRightsAbility.class
        };
        // 按照顺序加载类
        for (Class<?> cls : abilityList) {
            ClassUtil.loadClass(cls.getName());
            logger.info(new StringBuffer("加载能力组件：").append(cls.getName()).toString());
        }
    }
}
