package com.garlicts.framework.distributed;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.garlicts.framework.distributed.redis.JedisTemplate;

import redis.clients.jedis.Jedis;

/**
 * 服务的负载均衡
 * @author 水木星辰 
 */
public class ServiceLoadBalancer {
	
	Logger logger = LoggerFactory.getLogger(ServiceLoadBalancer.class);

	private String onlineServices = "onlineServices"; // 保存在线service的redis集合名称
	
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
			len = jedis.llen(onlineServices);
			tmp = len;
			List<String> services = jedis.lrange(onlineServices, 0, -1);
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
	 * 获取所有服务列表
	 */
	public List<String> getAllService(){
		
		Jedis jedis = JedisTemplate.getJedis();
		List<String> services = null;
		try{
			services = jedis.lrange(onlineServices, 0, -1);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			jedis.close();
		}
		
		return services;
		
	}
	
	/**
	 * 移除已经离线的服务 
	 */
	public boolean removeService(String serviceUrl){
		
		Jedis jedis = JedisTemplate.getJedis();
		try{
			
			Long lrem = jedis.lrem(onlineServices, 0, serviceUrl);
			if(lrem > 0){
				logger.info("服务serviceUrl离线，已被移除：" + serviceUrl);
				return true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			jedis.close();
		}
		
		return false;

	}
	
	/**
	 * 检测服务URL是否在线
	 * @param serviceUrl：http://192.168.0.100:8080/工程名 
	 */
	public boolean checkServiceStatus(String serviceUrl){
		
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
	 * 检测所有的服务是否在线，并移除离线的服务
	 */
	public void checkAllServiceStatus(){
		
		List<String> allService = getAllService();
		for(String serviceUrl : allService){
			if(!checkServiceStatus(serviceUrl)){
				// 移除已离线的ServiceUrl
				removeService(serviceUrl);
			}
		}
		
	}
	
	/**
	 * 随机路由到一个服务URL 
	 */
	public String route(){
		
		String serviceUrl = randomRouteServiceUrl();
		
		if(checkServiceStatus(serviceUrl)){
			return serviceUrl;
		}else{
			removeService(serviceUrl);
			serviceUrl = randomRouteServiceUrl();
		}
		
		return serviceUrl;
		
	}
	
	public String randomRouteServiceUrl(){
		
		List<String> list = getAllService();
		int size = list.size() > 0 ? list.size() : 0;
		if(size == 0){
			return null;
		}
		
		int random = (int) (Math.random() * size);
		String serviceUrl = (String) list.get(random);
		
		return serviceUrl;
		
	}
	
}
