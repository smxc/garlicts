package com.garlicts.framework.crawler.htmlunit;

import org.apache.commons.pool2.impl.GenericObjectPool;

import com.gargoylesoftware.htmlunit.WebClient;

public class WebClientPool extends GenericObjectPool<WebClient> {

	public WebClientPool() {
		super(new WebClientFactory(), new WebClientPoolConfig());
	}

}
