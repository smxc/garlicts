package com.garlicts.framework.util;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * JSON工具类
 * @author 水木星辰
 * @version 1.0.0 
 */
public class JsonUtil {

	public static final Gson gson = new Gson();
	
	/**
	 * 将对象转成JSON 
	 * 
	 */
	public static <T> String toJson(T object){
		String json = gson.toJson(object);
		return json;
	}

	
	
	/**
	 * 将JSON转成bean对象 
	 * 
	 */
	public static <T> T jsonToBean(String json, Class<T> cls){
		T t = gson.fromJson(json, cls);
		return t;
	}
	
	/**
	 * json转成list
	 *  
	 */
	public static <T> List<T> jsonToList(String json, Class<T> cls){
		List<T> list = gson.fromJson(json, new TypeToken<List<T>>(){}.getType());
		return list;
	}
	
	/**
	 * json转成泛型为map的list
	 * 
	 */
	public static <T> List<Map<String,T>> jsonToListForMap(String json){
		List<Map<String,T>> list = gson.fromJson(json, new TypeToken<List<Map<String,T>>>(){}.getType());
		return list;
	}
	
	/**
	 * json转成map
	 *  
	 */
	public static <T> Map<String,T> jsonToMap(String json){
		Map<String,T> map = gson.fromJson(json, new TypeToken<Map<String,T>>(){}.getType());
		return map;
	}
	
}
