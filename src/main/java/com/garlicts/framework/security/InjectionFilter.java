package com.garlicts.framework.security;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter(urlPatterns = {"/*"}, dispatcherTypes = DispatcherType.REQUEST)
public class InjectionFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(InjectionFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		logger.info("进入防注入过滤器：" + InjectionFilter.class.getName());
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		InjectionRequestWrapper injectionRequestWrapper = new InjectionRequestWrapper(httpServletRequest);
		chain.doFilter(injectionRequestWrapper, response);
		
	}

	@Override
	public void destroy() {
		
	}

	
	
}
