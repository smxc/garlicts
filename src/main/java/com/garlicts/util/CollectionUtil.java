package com.garlicts.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

//import org.apache.commons.collections.CollectionUtils;

/**
 * 集合操作工具类
 *
 * @author 水木星辰
 * @since 1.0
 */
public class CollectionUtil {

	// /**
	// * 判断集合是否非空
	// */
	// public static boolean isNotEmpty(Collection<?> collection) {
	// return CollectionUtils.isNotEmpty(collection);
	// }
	//
	// /**
	// * 判断集合是否为空
	// */
	// public static boolean isEmpty(Collection<?> collection) {
	// return CollectionUtils.isEmpty(collection);
	// }

	/**
	 * 获取指定的集合/数组/枚举的大小
	 * <p>
	 * 此方法可以处理如下对象：
	 * <ul>
	 * <li>Collection
	 * <li>Map
	 * <li>Array
	 * <li>Enumeration
	 * </ul>
	 *
	 * @param object
	 * @return
	 * @throws
	 * @since 1.0
	 */
	public static int size(final Object object) {
		if (object == null) {
			return 0;
		}
		int total = 0;
		if (object instanceof Map<?, ?>) {
			total = ((Map<?, ?>) object).size();
		} else if (object instanceof Collection<?>) {
			total = ((Collection<?>) object).size();
		} else if (object instanceof Object[]) {
			total = ((Object[]) object).length;
		} else if (object instanceof Enumeration<?>) {
			final Enumeration<?> it = (Enumeration<?>) object;
			while (it.hasMoreElements()) {
				total++;
				it.nextElement();
			}
		} else {
			try {
				total = Array.getLength(object);
			} catch (final IllegalArgumentException ex) {
				throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
			}
		}
		return total;
	}

	/**
	 * 检查指定的集合/数组/枚举为空
	 * <p>
	 * 此方法可以处理如下对象：
	 * <ul>
	 * <li>Collection
	 * <li>Map
	 * <li>Array
	 * <li>Enumeration
	 * </ul>
	 * <p>
	 *
	 * @param object
	 * @return true
	 * @throws IllegalArgumentException
	 * @since 1.0
	 */
	public static boolean isEmpty(final Object object) {
		if (object == null) {
			return true;
		} else if (object instanceof Collection<?>) {
			return ((Collection<?>) object).isEmpty();
		} else if (object instanceof Map<?, ?>) {
			return ((Map<?, ?>) object).isEmpty();
		} else if (object instanceof Object[]) {
			return ((Object[]) object).length == 0;
		} else if (object instanceof Iterator<?>) {
			return ((Iterator<?>) object).hasNext() == false;
		} else if (object instanceof Enumeration<?>) {
			return ((Enumeration<?>) object).hasMoreElements() == false;
		} else {
			try {
				return Array.getLength(object) == 0;
			} catch (final IllegalArgumentException ex) {
				throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
			}
		}
	}

	/**
	 * 检查指定的集合/数组/枚举不为空
	 * <p>
	 * 此方法可以处理如下对象：
	 * <ul>
	 * <li>Collection
	 * <li>Map
	 * <li>Array
	 * <li>Enumeration
	 * </ul>
	 * <p>
	 *
	 * @param object
	 * @return true
	 * @throws IllegalArgumentException
	 * @since 1.0
	 */
	public static boolean isNotEmpty(final Object object) {
		return !isEmpty(object);
	}

}
