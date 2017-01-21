package com.garlicts.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 映射操作工具类
 *
 * @author 水木星辰
 * @since 1.0
 */
public class MapUtil {

 /**
     * Null-safe check if the specified map is empty.
     * <p>
     * Null returns true.
     *
     * @param map  the map to check, may be null
     * @return true if empty or null
     * @since 3.2
     */
    public static boolean isEmpty(final Map<?,?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Null-safe check if the specified map is not empty.
     * <p>
     * Null returns false.
     *
     * @param map  the map to check, may be null
     * @return true if non-null and non-empty
     * @since 3.2
     */
    public static boolean isNotEmpty(final Map<?,?> map) {
        return !isEmpty(map);
    }


    /**
     * 转置 Map
     */
    public static <K, V> Map<V, K> invert(Map<K, V> source) {
        Map<V, K> target = new LinkedHashMap<V, K>(source.size());
        for (Map.Entry<K, V> entry : source.entrySet()) {
            target.put(entry.getValue(),entry.getKey());
        }
        return target;
    }
}
