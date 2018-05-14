package com.garlicts.framework.http;

import java.net.URI;
import java.nio.charset.Charset;

import org.junit.Test;

import com.garlicts.framework.http.converter.StringHttpMessageConverter;

public class SimpleClientHttpRequestFactoryTest {

	@Test
	public void test1(){
		
		SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		try {
			URI uri = new URI("http://www.baidu.com");
			ClientHttpRequest clientHttpRequest = simpleClientHttpRequestFactory.createRequest(uri, HttpMethod.GET);
			ClientHttpResponse clientHttpResponse = clientHttpRequest.execute();
			
			StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
			String read = stringHttpMessageConverter.read(String.class, clientHttpResponse);
			System.out.println(read);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void test2(){
		
		SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		try {
			URI uri = new URI("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=D20D1DD68D055B69DA9BB445A54C5B40&steamids=76561198100902124");
			ClientHttpRequest clientHttpRequest = simpleClientHttpRequestFactory.createRequest(uri, HttpMethod.GET);
			ClientHttpResponse clientHttpResponse = clientHttpRequest.executeJson();
			
			StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
			String read = stringHttpMessageConverter.read(String.class, clientHttpResponse);
			System.out.println(read);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
