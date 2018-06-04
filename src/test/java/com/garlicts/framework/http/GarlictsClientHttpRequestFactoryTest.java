package com.garlicts.framework.http;

import java.net.URI;
import java.nio.charset.Charset;

import org.junit.Test;

public class GarlictsClientHttpRequestFactoryTest {

	@Test
	public void test1(){
		
		GarlictsClientHttpRequestFactory simpleClientHttpRequestFactory = new GarlictsClientHttpRequestFactory();
		try {
			URI uri = new URI("http://www.baidu.com");
			ClientHttpRequest clientHttpRequest = simpleClientHttpRequestFactory.createRequest(uri, HttpMethod.GET);
			ClientHttpResponse clientHttpResponse = clientHttpRequest.execute();
			String bodyAsString = clientHttpResponse.getBodyAsString();
			System.out.println(bodyAsString);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void test2(){
		
		GarlictsClientHttpRequestFactory simpleClientHttpRequestFactory = new GarlictsClientHttpRequestFactory();
		try {
			URI uri = new URI("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=D20D1DD68D055B69DA9BB445A54C5B40&steamids=76561198100902124");
			ClientHttpRequest clientHttpRequest = simpleClientHttpRequestFactory.createRequest(uri, HttpMethod.GET);
			ClientHttpResponse clientHttpResponse = clientHttpRequest.executeJson();
			
			System.out.println(clientHttpResponse.getBodyAsString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void test3(){
		
		GarlictsClientHttpRequestFactory httpRequestFactory = new GarlictsClientHttpRequestFactory();
		try {
			URI uri = new URI("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=D20D1DD68D055B69DA9BB445A54C5B40&steamids=76561198100902124");
			ClientHttpRequest clientHttpRequest = httpRequestFactory.createRequest(uri, HttpMethod.GET);
			ClientHttpResponse clientHttpResponse = clientHttpRequest.execute();
			
			System.out.println(clientHttpResponse.getBodyAsString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
