package com.garlicts.security;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.garlicts.util.StringUtil;

/**
 * HttpServletRequest增强类
 * 
 * HttpServletRequestWrapper类实现了request接口中的所有方法，但这些方法的内部实现都是仅仅调用了 request对象的对应方法，
 * 这样做的目的是：避免用户在对request对象进行增强时需要实现request接口中的所有方法。所以当需要增强request对象时，
 * 只需要写一个类继承HttpServletRequestWrapper类，然后再重写需要增强的方法即可。
 * @since V1.0
 * @author 水木星辰
 */
public class CharacterEncodingRequestWrapper extends HttpServletRequestWrapper {

	private HttpServletRequest request;
	
	public CharacterEncodingRequestWrapper(HttpServletRequest request) {
		super(request);
		this.request = request;
	}
	
	@Override
	public String getParameter(String name) {
		
		String parameterValue = this.request.getParameter(name);
		if(StringUtil.isBlank(parameterValue)){
			return null;
		}
		
		if(!this.request.getMethod().equalsIgnoreCase("get")){
			return parameterValue;
		}else{
			try {
				parameterValue = new String(parameterValue.getBytes("ISO8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		return parameterValue;
		
	}

}
