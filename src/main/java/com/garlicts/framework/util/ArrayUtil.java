package com.garlicts.framework.util;

/**
 * <p>
 * 数组工具类
 * </p>
 * 
 * @author 水木星辰
 * @since 1.0.0
 */
public class ArrayUtil {

	/**
	 * <p>
	 * 检查一个Object数组为空或null
	 * </p>
	 *
	 * @param array
	 * @since 1.0.0
	 */
	public static boolean isEmpty(final Object[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * 检查一个对象数组不为空或null
	 * </p>
	 *
	 * @param array
	 * @since 1.0.0
	 */
	public static <T> boolean isNotEmpty(final T[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * 检查一个double数组为空或null
	 * </p>
	 *
	 * @param array
	 * @since 1.0.0
	 */
	public static boolean isEmpty(final double[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * 检查一个double数组不为空或null
	 * </p>
	 *
	 * @param array
	 * @since 1.0.0
	 */
	public static boolean isNotEmpty(final double[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * 检查一个float浮动数组为空或null
	 * </p>
	 *
	 * @param array
	 * @since 1.0.0
	 */
	public static boolean isEmpty(final float[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * 检查一个float浮动数组不为空或null
	 * </p>
	 *
	 * @param array
	 * @since 1.0.0
	 */
	public static boolean isNotEmpty(final float[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * 检查一个long数组为空或null
	 * </p>
	 *
	 * @param array
	 * @since 1.0.0
	 */
	public static boolean isEmpty(final long[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * 检查一个long数组不为空或null
	 * </p>
	 *
	 * @param array
	 * @since 1.0.0
	 */
	public static boolean isNotEmpty(final long[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * 检查一个int数组为空或null
	 * </p>
	 *
	 * @param array
	 * @since 1.0.0
	 */
	public static boolean isEmpty(final int[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * 检查一个int数组不为空或null
	 * </p>
	 *
	 * @param array
	 * @since 1.0.0
	 */
	public static boolean isNotEmpty(final int[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * 检查一个short数组为空或null
	 * </p>
	 *
	 * @param array
	 * @since 1.0.0
	 */
	public static boolean isEmpty(final short[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * 检查一个short数组不为空或null
	 * </p>
	 *
	 * @param array
	 * @since 1.0.0
	 */
	public static boolean isNotEmpty(final short[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * 检查一个char数组为空或null
	 * </p>
	 *
	 * @param array
	 * @since 1.0.0
	 */
	public static boolean isEmpty(final char[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * 检查一个char数组不为空或null
	 * </p>
	 *
	 * @param array
	 * @since 1.0.0
	 */
	public static boolean isNotEmpty(final char[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * 检查一个byte数组为空或null
	 * </p>
	 *
	 * @param array
	 * @since 1.0.0
	 */
	public static boolean isEmpty(final byte[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * 检查一个byte数组不为空或null
	 * </p>
	 *
	 * @param array
	 * @since 1.0.0
	 */
	public static boolean isNotEmpty(final byte[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * 检查一个boolean数组为空或null
	 * </p>
	 *
	 * @param array
	 * @since 1.0.0
	 */
	public static boolean isEmpty(final boolean[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * 检查一个boolean数组不为空或null
	 * </p>
	 *
	 * @param array
	 * @since 1.0.0
	 */
	public static boolean isNotEmpty(final boolean[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling
	 * <code>null</code>.
	 * </p>
	 *
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array
	 *            the array to clone, may be <code>null</code>
	 * @return the cloned array, <code>null</code> if <code>null</code> input
	 */
	public static float[] clone(float[] array) {
		if (array == null) {
			return null;
		}
		return (float[]) array.clone();
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling
	 * <code>null</code>.
	 * </p>
	 *
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array
	 *            the array to clone, may be <code>null</code>
	 * @return the cloned array, <code>null</code> if <code>null</code> input
	 */
	public static Object[] clone(Object[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of <code>array1</code> followed
	 * by all of the elements <code>array2</code>. When an array is returned, it
	 * is always a new array.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 *
	 * @param array1
	 *            the first array whose elements are added to the new array.
	 * @param array2
	 *            the second array whose elements are added to the new array.
	 * @return The new double[] array.
	 * @since 2.1
	 */
	public static Object[] addAll(Object[] array1, Object[] array2) {
		if (array1 == null) {
			return clone(array2);
		} else if (array2 == null) {
			return clone(array1);
		}
		Object[] joinedArray = new Object[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

    /**
     * 连接数组
     */
    public static Object[] concat(Object[] array1, Object[] array2) {
        return addAll(array1, array2);
    }	
	
}
