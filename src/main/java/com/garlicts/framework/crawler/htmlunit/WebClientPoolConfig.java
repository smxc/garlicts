package com.garlicts.framework.crawler.htmlunit;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class WebClientPoolConfig extends GenericObjectPoolConfig {

	public WebClientPoolConfig(){
		setMinIdle(4);
		setTestOnBorrow(true);
	}
	
}
