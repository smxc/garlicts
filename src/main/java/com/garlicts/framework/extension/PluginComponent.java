package com.garlicts.framework.extension;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.garlicts.framework.FrameworkConstant;
import com.garlicts.framework.InstanceFactory;
import com.garlicts.framework.core.BeanLoaderTemplate;
import com.garlicts.framework.core.fault.InitializationError;

/**
 * 初始化插件能力
 *
 * @author 水木星辰
 * @since 1.0
 */
public class PluginComponent {

	private static final Logger logger = LoggerFactory.getLogger(PluginComponent.class);
	
    /**
     * 创建一个插件列表（用于存放插件实例）
     */
    private static final List<Plugin> pluginList = new LinkedList<Plugin>();

    private static final BeanLoaderTemplate beanLoader = InstanceFactory.getBeanLoaderTemplate();

    static {
        try {
        	
            // 获取并遍历所有的插件类（实现了 Plugin 接口的类）
            List<Class<?>> pluginClassList = beanLoader.getBeanClassListBySuper(FrameworkConstant.PLUGIN_PACKAGE, Plugin.class);
            
            for (Class<?> pluginClass : pluginClassList) {
                // 创建插件实例
                Plugin plugin = (Plugin) pluginClass.newInstance();
                // 调用初始化方法
                plugin.init();
                // 将插件实例添加到插件列表中
                pluginList.add(plugin);
                logger.info("初始化插件：" + plugin.getClass().getName());
            }

        } catch (Exception e) {
            throw new InitializationError("初始化 PluginAbility 出错！");
        }
    }

    /**
     * 获取所有插件
     */
    public static List<Plugin> getPluginList() {
        return pluginList;
    }
}
