package com.garlicts.framework.distributed;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 服务的负载均衡
 * @author 水木星辰 
 */
public class ServiceLoadBalancer {

	/**
	 * 判断服务URL是否在线
	 * serviceUrl：http://192.168.0.100:8080/工程名 
	 */
	private boolean serviceIsOnline(String serviceUrl){
		
		HttpURLConnection httpURLConnection = null;
		try {
			httpURLConnection = (HttpURLConnection) new URL(serviceUrl).openConnection();
			int responseCode = httpURLConnection.getResponseCode();
			if(responseCode == 200){
				return true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			
			if(httpURLConnection != null){
				httpURLConnection.disconnect();
			}
			
		}
		
		return false;
		
		
	}
	
	/**
	 * 随机路由到一个服务URL 
	 */
	public String route(){
		return "";
	}
	
	public void checkServiceUrl(){
		
	}
	
}
