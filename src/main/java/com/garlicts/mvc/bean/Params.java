package com.garlicts.mvc.bean;

import java.util.Map;

import com.garlicts.core.bean.BaseBean;
import com.garlicts.util.ConvertUtil;

/**
 * 封装请求参数
 *
 * @author 水木星辰
 * @since 1.0
 */
public class Params extends BaseBean {

	private static final long serialVersionUID = 5651447603069799068L;
	
	private final Map<String, Object> fieldMap;

    public Params(Map<String, Object> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public Map<String, Object> getFieldMap() {
        return fieldMap;
    }

    public String getString(String name) {
        return ConvertUtil.convertToString(get(name));
    }

    public double getDouble(String name) {
        return ConvertUtil.convertToDouble(get(name));
    }

    public long getLong(String name) {
        return ConvertUtil.convertToLong(get(name));
    }

    public int getInt(String name) {
        return ConvertUtil.convertToInt(get(name));
    }

    private Object get(String name) {
        return fieldMap.get(name);
    }
}
