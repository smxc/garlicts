package com.garlicts.config;

import java.util.Map;
import java.util.Properties;

import com.garlicts.FrameworkConstant;

/**
 * 获取属性文件中的属性值
 *
 * @author 水木星辰
 * @since 1.0
 */
public class PropertiesProvider {

    /**
     * 属性文件对象
     */
    private static final Properties configProps = PropertiesLoader.loadProps(FrameworkConstant.CONFIG_FILE);

    /**
     * 获取 String 类型的属性值
     */
    public static String getString(String key) {
        return PropertiesLoader.getString(configProps, key);
    }

    /**
     * 获取 String 类型的属性值（可指定默认值）
     */
    public static String getString(String key, String defaultValue) {
        return PropertiesLoader.getString(configProps, key, defaultValue);
    }

    /**
     * 获取 int 类型的属性值
     */
    public static int getInt(String key) {
        return PropertiesLoader.getNumber(configProps, key);
    }

    /**
     * 获取 int 类型的属性值（可指定默认值）
     */
    public static int getInt(String key, int defaultValue) {
        return PropertiesLoader.getNumber(configProps, key, defaultValue);
    }

    /**
     * 获取 boolean 类型的属性值
     */
    public static boolean getBoolean(String key) {
        return PropertiesLoader.getBoolean(configProps, key);
    }

    /**
     * 获取 int 类型的属性值（可指定默认值）
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        return PropertiesLoader.getBoolean(configProps, key, defaultValue);
    }

    /**
     * 获取指定前缀的相关属性
     *
     * @since 1.0
     */
    public static Map<String, Object> getMap(String prefix) {
        return PropertiesLoader.getMap(configProps, prefix);
    }
}
