package com.garlicts.framework.distributed;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import redis.clients.jedis.Jedis;

import com.garlicts.framework.plugin.cache.redis.JedisTemplate;

/**
 * 服务的负载均衡
 * @author 水木星辰 
 */
public class ServiceLoadBalancer {

	// 刷新时间，单位为秒
	private int refreshSeconds = 60;
	
	private static ServiceLoadBalancer serviceLoadBalancer = new ServiceLoadBalancer();
	
	public ServiceLoadBalancer getInstance(){
		return serviceLoadBalancer;
	}
	
	/**
	 * 注册服务
	 * 将服务URL保存到redis的list集合中
	 * @param serviceUrl：http://192.168.0.100:8080/工程名 
	 */
	public boolean registerService(String serviceUrl){
		
		Jedis jedis = JedisTemplate.getJedis();
		Long len;
		Long tmp;
		try{
			len = jedis.llen("onlineServices");
			tmp = len;
			List<String> services = jedis.lrange("onlineServices", 0, -1);
			if(!services.contains(serviceUrl)){
				len = jedis.rpush("onlineServices", serviceUrl);
				if(tmp + 1 == len){
					return true;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			jedis.close();
		}
		
		return false;
		
	}
	
	/**
	 * 移除服务 
	 */
	public boolean removeService(){
		return false;
	}
	
	/**
	 * 判断服务URL是否在线
	 * @param serviceUrl：http://192.168.0.100:8080/工程名 
	 */
	public boolean serviceIsOnline(String serviceUrl){
		
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
	
	/**
	 * 检测服务URL是否在线 
	 */
	public void checkServiceUrl(){
		
	}
	
}
