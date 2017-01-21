package com.garlicts;

import org.slf4j.Logger;

import com.garlicts.aop.AopProxyAbility;
import com.garlicts.dao.DatabaseAbility;
import com.garlicts.ioc.BeanContainerAbility;
import com.garlicts.ioc.IocAbility;
import org.slf4j.LoggerFactory;
import com.garlicts.mvc.ControllerAbility;
import com.garlicts.plugin.PluginAbility;
import com.garlicts.util.ClassUtil;

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
            IocAbility.class
        };
        // 按照顺序加载类
        for (Class<?> cls : abilityList) {
            ClassUtil.loadClass(cls.getName());
            logger.info(cls.getName() + " 启动成功 ");
        }
    }
}
