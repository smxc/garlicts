package com.garlicts.framework.crawler.htmlunit;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.garlicts.framework.util.StringUtil;

public abstract class HtmlUnitCrawler {

	private static final Logger LOGGER = LoggerFactory.getLogger(HtmlUnitCrawler.class);
	
	// HttpUnit WebClient的对象池
	WebClientPool webClientPool;
	
	public HtmlUnitCrawler(){
		webClientPool = new WebClientPool();
	}
	
	private WebClient create(){
		
		WebClient webClient = null;
		
		try {
			webClient = webClientPool.borrowObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return webClient;
		
	}
	
	public void execute(CrawlerHttpRequest crawlerHttpRequest){
		
		String key = crawlerHttpRequest.getKey();
		String url = crawlerHttpRequest.getUrl();
		WebClient webClient = create();
		
		HtmlPage htmlPage = null;
		
		try {
			WebRequest webRequest = buildWebRequest(crawlerHttpRequest);
			htmlPage = webClient.getPage(webRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebResponse webResponse = htmlPage.getWebResponse();
		int statusCode = webResponse.getStatusCode();
		if(statusCode != 200){
			StringBuilder logStr = new StringBuilder();
			logStr.append("爬取url[").append(url).append("]失败，http status code is ").append(statusCode);
			LOGGER.error(logStr.toString());
		}
		
		handle(htmlPage, key);
		
		webClientPool.returnObject(webClient);
		
	}
	
	public WebRequest buildWebRequest(CrawlerHttpRequest crawlerHttpRequest){
		
		String url = crawlerHttpRequest.getUrl();
		Map<String,String> additionalHeaders = crawlerHttpRequest.getAdditionalHeaders();
		String proxyAddress = crawlerHttpRequest.getProxyAddress();
		
		URL link;
		WebRequest webRequest = null;
		
		try {
			link = new URL(url);
			webRequest = new WebRequest(link);
			
			if(StringUtil.isNotEmpty(proxyAddress)){
				
				String[] proxyAddr = proxyAddress.split(proxyAddress);
				
				webRequest.setProxyHost(proxyAddr[0]);
				webRequest.setProxyPort(Integer.parseInt(proxyAddr[1]));
				
			}

			webRequest.setAdditionalHeaders(additionalHeaders);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return webRequest;
	}	
	
	public abstract void handle(HtmlPage htmlPage, String key);
	
}
