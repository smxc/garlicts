package com.garlicts.framework.crawler.htmlunit;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;

public class WebClientFactory extends BasePooledObjectFactory<WebClient> {

	// 创建池子对象
	@Override
	public WebClient create() throws Exception {
		
		WebClient webClient = new WebClient();
		
		// 开启js解析
		webClient.getOptions().setJavaScriptEnabled(true);
		// 设置JS执行的超时时间
		webClient.setJavaScriptTimeout(30000);
		webClient.getOptions().setCssEnabled(false);
		// 开启cookie管理
		webClient.getCookieManager().setCookiesEnabled(true);
		// js语法错误抛出异常，继续执行
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		// 设置连接超时时间 ，这里是10S。如果为0，则无限期等待  
		webClient.getOptions().setTimeout(10000);
		// 让游览行为被记录
		webClient.getOptions().setDoNotTrackEnabled(true);
		// 设置JS后台等待执行时间
		webClient.waitForBackgroundJavaScript(30000);		
		// 启用ajax支持
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		
		return webClient;
		
	}

	@Override
	public PooledObject<WebClient> wrap(WebClient webClient) {
		return new DefaultPooledObject<WebClient>(webClient);
	}

}
