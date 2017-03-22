package com.garlicts.framework.mvc;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

import com.garlicts.framework.AbilityLoader;
import com.garlicts.framework.FrameworkConstant;
import com.garlicts.framework.InitializeData;
import com.garlicts.framework.InstanceFactory;
import com.garlicts.framework.config.PropertiesProvider;
import com.garlicts.framework.core.BeanLoaderTemplate;
import com.garlicts.framework.ioc.BeanContainerAbility;
import com.garlicts.framework.plugin.Plugin;
import com.garlicts.framework.plugin.PluginAbility;
import com.garlicts.framework.util.StringUtil;

/**
 * 容器监听器
 *
 * @author 水木星辰
 * @since 1.0
 */
@WebListener
public class ContainerListener implements ServletContextListener {

	private static final BeanLoaderTemplate beanLoaderTemplate = InstanceFactory.getBeanLoaderTemplate();
	
    /**
     * 当容器初始化时调用
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 获取 ServletContext
        ServletContext servletContext = sce.getServletContext();
        // 初始化所有能力
        AbilityLoader.init();
        // 添加 Servlet 映射
        addServletMapping(servletContext);
        // 注册 WebPlugin
        registerPlugin(servletContext);
        // 调用初始化类
        invokeInitData();
    }

    /**
     * 当容器销毁时调用
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 销毁插件
        destroyPlugin();
        // 销毁Bean容器
        BeanContainerAbility.destroy();
    }

    private void addServletMapping(ServletContext context) {
        // 用 DefaultServlet 映射所有静态资源
        registerDefaultServlet(context);
        // 用 JspServlet 映射所有 JSP 请求
        registerJspServlet(context);
    }

    private void registerDefaultServlet(ServletContext context) {
        ServletRegistration defaultServlet = context.getServletRegistration("default");
        defaultServlet.addMapping("/index.html", "/favicon.ico");
        String wwwPath = PropertiesProvider.getString(FrameworkConstant.WWW_PATH);
        if (StringUtil.isNotEmpty(wwwPath)) {
            defaultServlet.addMapping(wwwPath + "*");
        }
    }

    private void registerJspServlet(ServletContext context) {
        ServletRegistration jspServlet = context.getServletRegistration("jsp");
        jspServlet.addMapping("/index.jsp");
        String jspPath = PropertiesProvider.getString(FrameworkConstant.JSP_PATH);
        if (StringUtil.isNotEmpty(jspPath)) {
            jspServlet.addMapping(jspPath + "*");
        }
    }

    private void registerPlugin(ServletContext servletContext) {
        List<Plugin> pluginList = PluginAbility.getPluginList();
        for (Plugin plugin : pluginList) {
        	
//        	// web插件
//            if (plugin instanceof WebPlugin) {
//                WebPlugin webPlugin = (WebPlugin) plugin;
//                webPlugin.register(servletContext);
//            }
        	
//            // 分布式插件
//            else if(plugin instanceof DistributedPlugin){
//            	DistributedPlugin distributedPlugin = (DistributedPlugin) plugin;
//            	distributedPlugin.init();
//            }
        	
        	plugin.init();
        	
        }
    }

    private void destroyPlugin() {
        List<Plugin> pluginList = PluginAbility.getPluginList();
        for (Plugin plugin : pluginList) {
            plugin.destroy();
        }
    }
    
    @SuppressWarnings(value="all")
    private void invokeInitData(){
    	List<Class<?>> initDataClassList = beanLoaderTemplate.getBeanClassListBySuper(FrameworkConstant.BASE_PACKAGE, InitializeData.class);
    	for(Class<?> cls : initDataClassList){
    		try {
    			Object initInstatnce = BeanContainerAbility.getBean(cls);
				Method initMethod = cls.getDeclaredMethod("init", null);
				initMethod.setAccessible(true);
				initMethod.invoke(initInstatnce, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    }
    
}