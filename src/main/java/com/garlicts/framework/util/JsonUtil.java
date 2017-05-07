package com.garlicts.framework.util;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * JSON工具类
 * @author 水木星辰
 * @version 1.0.0 
 */
public class JsonUtil {

	/**
	 * 将对象转成JSON 
	 * 
	 * 例1：将Map转成JSON
	 * Map<String, Object> map = new HashMap<String, Object>();
	 * map.put("key1", "One");
	 * map.put("key2", "Two");
	 * String mapJson = JSON.toJSONString(map);
	 * 输出：{"key1":"One","key2":"Two"}
	 * 
	 * 例2：将List<Map>转成JSON
	 * List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	 * Map<String, Object> map1 = new HashMap<String, Object>();
	 * map1.put("key1", "One");
	 * map1.put("key2", "Two");
	 * Map<String, Object> map2 = new HashMap<String, Object>();
	 * map2.put("key1", "Three");
	 * map2.put("key2", "Four");
	 * list.add(map1);
	 * list.add(map2);
	 * 输出结果：[{"key1":"One","key2":"Two"},{"key3":"Three","key4":"Four"}]
	 * 
	 * 例3：自定义JavaBean User转成JSON
	 * User user = new User();
	 * user.setUserName("李四");
	 * user.setAge(24);
	 * String userJson = JSON.toJSONString(user);
	 * 输出结果：{"age":24,"userName":"李四"}
	 * 
	 * 例4：指定输出日期格式
	 * String dateJson = JSON.toJSONStringWithDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
	 * 
	 * 例5：缺省情况下FastJSON不输入为值Null的字段，可以使用SerializerFeature.WriteMapNullValue使其输出。
	 * Map<String, Object> map = new HashMap<String,Object>();
	 * String b = null;
	 * Integer i = 1;
	 * map.put("a", b);
	 * map.put("b", i);
	 * String listJson = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
	 * 输出结果：{"a":null,"b":1}
	 */
	public static <T> String toJson(T object){
		String jsonStr = JSON.toJSONString(object);
		return jsonStr;
	}
	
	/**
	 * 将JSON转成bean对象 
	 * 
	 */
	public static <T> T jsonToBean(String json, Class<T> cls){
		T t = JSON.parseObject(json, cls);
		return t;
	}
	
	/**
	 * json转成List<bean>或List<String>
	 *  
	 */
	public static <T> List<T> jsonToList(String json, Class<T> cls){
		List<T> list = JSON.parseArray(json, cls);
		return list;
	}
	
	/**
	 * json转成List<Map<String,Object>>
	 * 
	 */
	public static List<Map<String,Object>> jsonToMapList(String json){
		List<Map<String,Object>> list = JSON.parseObject(json, new TypeReference<List<Map<String,Object>>>(){});
		return list;
	}
	
	/**
	 * json转成map
	 *  
	 */
	public static Map<String,Object> jsonToMap(String json){
		Map<String, Object> map = (Map<String,Object>)JSON.parseObject(json);
		return map;
	}
	
}
