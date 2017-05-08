package com.garlicts.framework.util;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class BeanUtil {

	public static <T> T mapToBean(Map<String, Object> map, Class<T> cls){
		
		T bean = null;
		
		try {
			bean = cls.newInstance();
			// 将map中的数据封装到javabean
			BeanUtils.populate(bean, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bean;
		
	}
	
}
