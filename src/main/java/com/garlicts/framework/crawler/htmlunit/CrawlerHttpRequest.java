package com.garlicts.framework.crawler.htmlunit;

import java.util.Map;

public class CrawlerHttpRequest {

	// 爬去的Url
	public String url;
	
	// http header
	public Map<String,String> additionalHeaders;
	
	// 关键字
	public String key;
	
	// 代理ip:port
	public String proxyAddress;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getAdditionalHeaders() {
		return additionalHeaders;
	}

	public void setAdditionalHeaders(Map<String, String> additionalHeaders) {
		this.additionalHeaders = additionalHeaders;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getProxyAddress() {
		return proxyAddress;
	}

	public void setProxyAddress(String proxyAddress) {
		this.proxyAddress = proxyAddress;
	}
	
}
