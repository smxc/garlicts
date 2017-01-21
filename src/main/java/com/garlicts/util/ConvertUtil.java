package com.garlicts.util;

/**
 * 转型操作工具类
 *
 * @author 水木星辰
 * @since 1.0
 */
public class ConvertUtil {

    /**
     * 转为 String 型
     */
    public static String convertToString(Object obj) {
        return ConvertUtil.convertToString(obj, "");
    }

    /**
     * 转为 String 型（提供默认值）
     */
    public static String convertToString(Object obj, String defaultValue) {
        return obj != null ? String.valueOf(obj) : defaultValue;
    }

    /**
     * 转为 double 型
     */
    public static double convertToDouble(Object obj) {
        return ConvertUtil.convertToDouble(obj, 0);
    }

    /**
     * 转为 double 型（提供默认值）
     */
    public static double convertToDouble(Object obj, double defaultValue) {
        double doubleValue = defaultValue;
        if (obj != null) {
            String strValue = convertToString(obj);
            if (StringUtil.isNotEmpty(strValue)) {
                try {
                    doubleValue = Double.parseDouble(strValue);
                } catch (NumberFormatException e) {
                    doubleValue = defaultValue;
                }
            }
        }
        return doubleValue;
    }

    /**
     * 转为 long 型
     */
    public static long convertToLong(Object obj) {
        return ConvertUtil.convertToLong(obj, 0);
    }

    /**
     * 转为 long 型（提供默认值）
     */
    public static long convertToLong(Object obj, long defaultValue) {
        long longValue = defaultValue;
        if (obj != null) {
            String strValue = convertToString(obj);
            if (StringUtil.isNotEmpty(strValue)) {
                try {
                    longValue = Long.parseLong(strValue);
                } catch (NumberFormatException e) {
                    longValue = defaultValue;
                }
            }
        }
        return longValue;
    }

    /**
     * 转为 int 型
     */
    public static int convertToInt(Object obj) {
        return ConvertUtil.convertToInt(obj, 0);
    }

    /**
     * 转为 int 型（提供默认值）
     */
    public static int convertToInt(Object obj, int defaultValue) {
        int intValue = defaultValue;
        if (obj != null) {
            String strValue = convertToString(obj);
            if (StringUtil.isNotEmpty(strValue)) {
                try {
                    intValue = Integer.parseInt(strValue);
                } catch (NumberFormatException e) {
                    intValue = defaultValue;
                }
            }
        }
        return intValue;
    }

    /**
     * 转为 boolean 型
     */
    public static boolean convertToBoolean(Object obj) {
        return ConvertUtil.convertToBoolean(obj, false);
    }

    /**
     * 转为 boolean 型（提供默认值）
     */
    public static boolean convertToBoolean(Object obj, boolean defaultValue) {
        boolean booleanValue = defaultValue;
        if (obj != null) {
            booleanValue = Boolean.parseBoolean(convertToString(obj));
        }
        return booleanValue;
    }

    /**
     * 转为 String[] 型
     */
    public static String[] convertToStringArray(Object[] objArray) {
        if (objArray == null) {
            objArray = new Object[0];
        }
        String[] strArray = new String[objArray.length];
        if (ArrayUtil.isNotEmpty(objArray)) {
            for (int i = 0; i < objArray.length; i++) {
                strArray[i] = convertToString(objArray[i]);
            }
        }
        return strArray;
    }

    /**
     * 转为 double[] 型
     */
    public static double[] convertToDoubleArray(Object[] objArray) {
        if (objArray == null) {
            objArray = new Object[0];
        }
        double[] doubleArray = new double[objArray.length];
        if (!ArrayUtil.isEmpty(objArray)) {
            for (int i = 0; i < objArray.length; i++) {
                doubleArray[i] = convertToDouble(objArray[i]);
            }
        }
        return doubleArray;
    }

    /**
     * 转为 long[] 型
     */
    public static long[] convertToLongArray(Object[] objArray) {
        if (objArray == null) {
            objArray = new Object[0];
        }
        long[] longArray = new long[objArray.length];
        if (!ArrayUtil.isEmpty(objArray)) {
            for (int i = 0; i < objArray.length; i++) {
                longArray[i] = convertToLong(objArray[i]);
            }
        }
        return longArray;
    }

    /**
     * 转为 int[] 型
     */
    public static int[] convertToIntArray(Object[] objArray) {
        if (objArray == null) {
            objArray = new Object[0];
        }
        int[] intArray = new int[objArray.length];
        if (!ArrayUtil.isEmpty(objArray)) {
            for (int i = 0; i < objArray.length; i++) {
                intArray[i] = convertToInt(objArray[i]);
            }
        }
        return intArray;
    }

    /**
     * 转为 boolean[] 型
     */
    public static boolean[] convertToBooleanArray(Object[] objArray) {
        if (objArray == null) {
            objArray = new Object[0];
        }
        boolean[] booleanArray = new boolean[objArray.length];
        if (!ArrayUtil.isEmpty(objArray)) {
            for (int i = 0; i < objArray.length; i++) {
                booleanArray[i] = convertToBoolean(objArray[i]);
            }
        }
        return booleanArray;
    }
}
