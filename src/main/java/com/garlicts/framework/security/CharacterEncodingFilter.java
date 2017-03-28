package com.garlicts.framework.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 编码过滤器
 * 此过滤器用来解决解决get、post请求方式下的中文乱码问题 
 * @since 1.0
 * @author 水木星辰
 */
@WebFilter(urlPatterns={"*.jsp","*.do","/services/*"})
public class CharacterEncodingFilter implements Filter {
	
	private final Logger logger = LoggerFactory.getLogger(CharacterEncodingFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(httpServletRequest.getRequestURI()).append(" 进入编码过滤器：").append(CharacterEncodingFilter.class.getName());
		logger.info(stringBuffer.toString());
		
		httpServletRequest.setCharacterEncoding("UTF-8");
		httpServletResponse.setCharacterEncoding("UTF-8");
		httpServletResponse.setContentType("text/html;charset=UTF-8");
		
		CharacterEncodingRequestWrapper characterEncodingRequestWrapper = new CharacterEncodingRequestWrapper(httpServletRequest);
		chain.doFilter(characterEncodingRequestWrapper, httpServletResponse);
		
	}

	@Override
	public void destroy() {
		
	}

}
