package com.garlicts.framework.plugin;

import java.util.ArrayList;
import java.util.List;

import com.garlicts.framework.FrameworkConstant;
import com.garlicts.framework.InstanceFactory;
import com.garlicts.framework.config.PropertiesProvider;
import com.garlicts.framework.core.BeanLoaderTemplate;
import com.garlicts.framework.core.fault.InitializationError;
import com.garlicts.framework.util.StringUtil;

/**
 * 初始化插件能力
 *
 * @author 水木星辰
 * @since 1.0
 */
public class PluginAbility {

    /**
     * 创建一个插件列表（用于存放插件实例）
     */
    private static final List<Plugin> pluginList = new ArrayList<Plugin>();

    private static final BeanLoaderTemplate beanLoader = InstanceFactory.getBeanLoaderTemplate();

    static {
        try {
            // 获取并遍历所有的插件类（实现了 Plugin 接口的类）
        	String plugin_package = PropertiesProvider.getString(FrameworkConstant.PLUGIN_PACKAGE);
        	if(StringUtil.isNotEmpty(plugin_package)){
                List<Class<?>> pluginClassList = beanLoader.getBeanClassListBySuper(plugin_package, Plugin.class);
                for (Class<?> pluginClass : pluginClassList) {
                    // 创建插件实例
                    Plugin plugin = (Plugin) pluginClass.newInstance();
                    // 调用初始化方法
                    plugin.init();
//                    // 调用注册方法
//                    plugin.register();
                    // 将插件实例添加到插件列表中
                    pluginList.add(plugin);
                    
                }        		
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
