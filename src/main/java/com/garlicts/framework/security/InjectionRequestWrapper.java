package com.garlicts.framework.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.garlicts.framework.util.StringUtil;

/**
 * 防注入
 * 防Xss攻击（跨站脚本攻击）和SQL注入
 * @since 1.0
 * @author 水木星辰
 */
public class InjectionRequestWrapper extends HttpServletRequestWrapper {

	
	public InjectionRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	
	public String[] getParameterValues(String name){
		
		String[] values = super.getParameterValues(name);
		if(values == null){
			return null;
		}
		
		String[] filteredValues = new String[values.length];
		for(int i = 0; i< values.length; i++){
			filteredValues[i] = injectionFilter(values[i]);
		}
		
		return filteredValues;
		
	}
	
	public String getParameter(String name){
		
		String value = super.getParameter(name);
		if(value == null){
			return null;
		}
		
		return injectionFilter(value);
		
	}
	
	public String getHeader(String name){
		
		String value = super.getHeader(name);
		if(value == null){
			return null;
		}
		
		return injectionFilter(value);
		
	}

	public String injectionFilter(String str){
		
		if(StringUtil.isBlank(str)){
			return null;
		}
		
		str = str.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		str = str.replaceAll("'", "&#39;");
		str = str.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		str = str.replaceAll("eval\\((.*)\\)", "");
//		str = str.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		str = str.replaceAll("script", "");
		
		return str;
		
	}
	
}

