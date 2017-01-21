package com.garlicts;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.garlicts.config.PropertiesProvider;
import com.garlicts.core.BeanLoaderTemplate;
import com.garlicts.dao.JdbcTemplate;
import com.garlicts.datasource.DataSourceFactory;
import com.garlicts.datasource.impl.DefaultDataSourceFactory;
import com.garlicts.mvc.HandlerExceptionResolver;
import com.garlicts.mvc.HandlerInvoker;
import com.garlicts.mvc.HandlerMapping;
import com.garlicts.mvc.ViewResolver;
import com.garlicts.mvc.impl.DefaultHandlerExceptionResolver;
import com.garlicts.mvc.impl.DefaultHandlerInvoker;
import com.garlicts.mvc.impl.DefaultHandlerMapping;
import com.garlicts.mvc.impl.DefaultViewResolver;
import com.garlicts.util.ClassUtil;
import com.garlicts.util.StringUtil;

/**
 * 实例工厂
 *
 * @author 水木星辰
 * @since 1.0
 */
public class InstanceFactory {

    /**
     * 用于缓存对应的实例
     */
    private static final Map<String, Object> cache = new ConcurrentHashMap<String, Object>();

    /**
     * 获取 BeanLoaderTemplate
     */
    public static BeanLoaderTemplate getBeanLoaderTemplate() {
        return getInstance(FrameworkConstant.BEAN_LOADER_TEMPLATE, BeanLoaderTemplate.class);
    }

    /**
     * 获取 DataSourceFactory
     */
    public static DataSourceFactory getDataSourceFactory() {
        return getInstance(FrameworkConstant.DS_FACTORY, DefaultDataSourceFactory.class);
    }

    /**
     * 获取 DataAccessor
     */
    public static JdbcTemplate getJdbcTemplate() {
        return getInstance(FrameworkConstant.JDBC_TEMPLATE, JdbcTemplate.class);
    }

    /**
     * 获取 HandlerMapping
     */
    public static HandlerMapping getHandlerMapping() {
        return getInstance(FrameworkConstant.HANDLER_MAPPING, DefaultHandlerMapping.class);
    }

    /**
     * 获取 HandlerInvoker
     */
    public static HandlerInvoker getHandlerInvoker() {
        return getInstance(FrameworkConstant.HANDLER_INVOKER, DefaultHandlerInvoker.class);
    }

    /**
     * 获取 HandlerExceptionResolver
     */
    public static HandlerExceptionResolver getHandlerExceptionResolver() {
        return getInstance(FrameworkConstant.HANDLER_EXCEPTION_RESOLVER, DefaultHandlerExceptionResolver.class);
    }

    /**
     * 获取 ViewResolver
     */
    public static ViewResolver getViewResolver() {
        return getInstance(FrameworkConstant.VIEW_RESOLVER, DefaultViewResolver.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(String cacheKey, Class<T> defaultImplClass) {
        // 若缓存中存在对应的实例，则返回该实例
        if (cache.containsKey(cacheKey)) {
            return (T) cache.get(cacheKey);
        }
        // 从配置文件中获取相应的接口实现类配置
        String implClassName = PropertiesProvider.getString(cacheKey);
        // 若实现类配置不存在，则使用默认实现类
        if (StringUtil.isEmpty(implClassName)) {
            implClassName = defaultImplClass.getName();
        }
        // 通过反射创建该实现类对应的实例
        //T instance = ObjectUtil.newInstance(implClassName);
        Class<?> implClass = ClassUtil.loadClass(implClassName);
        T instance = null;
		try {
			instance = (T) implClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        // 若该实例不为空，则将其放入缓存
        if (instance != null) {
            cache.put(cacheKey, instance);
        }
        // 返回该实例
        return instance;
    }
}
