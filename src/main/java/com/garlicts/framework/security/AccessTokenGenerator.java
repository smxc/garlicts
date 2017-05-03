package com.garlicts.framework.security;

import java.util.UUID;

import redis.clients.jedis.Jedis;

import com.garlicts.framework.plugin.cache.redis.JedisTemplate;

/**
 * AccessToken生成器
 * 每次调用接口之前，生成AccessToken，请保存到redis，接口层从redis获取AccessToken，并与前端应用传过来
 * 的AccessToken进行匹配，匹配成功后，认为接口调用合法，并从redis中删除该AccessToken。
 * 
 * 应用场景：在调用接口时，需要提供AccessToken
 * 
 * 如何获取AccessTokenGenerator实例：
 * 通过InstanceFactory.getAccessTokenGenerator();
 * 
 * 使用内部类实现lazy单例
 * 
 * @author 水木星辰 
 */
public class AccessTokenGenerator {

	private AccessTokenGenerator(){
		
	}
	
	private static class AccessTokenGeneratorInner{
		private final static AccessTokenGenerator instance = new AccessTokenGenerator();
	}
	
	public static AccessTokenGenerator getInstance(){
		return AccessTokenGeneratorInner.instance;
	}

	/**
	 * 生成accessToken，返回key，并将accessToken保存到redis 
	 */
	private String generateAccessToken(){

		Jedis jedis = null;
		String accessToken = null;
		
		try{

			accessToken = UUID.randomUUID().toString().replace("-", "");
			jedis = JedisTemplate.getJedis();
			jedis.setex(accessToken, 60, accessToken); // 60秒的超时时间
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			jedis.close();
		}
		
		return accessToken;
		
	}
	
	/**
	 * 生成并返回accessToken的key 
	 */
	public String getAccessTokenKey(){
		return generateAccessToken();
	}
	
	/**
	 * 通过key从redis获取对应的accessToken的value
	 */
	public String getAccessTokenValue(String key){
		
		Jedis jedis = null;
		String value = null;
		
		try{
			jedis = JedisTemplate.getJedis();
			value = jedis.get(key);
			return value;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			jedis.close();
		}
		
		return value;
		
	}
	
	public static void main(String[] args) {
		
		AccessTokenGenerator instance = AccessTokenGenerator.getInstance();
		String accessTokenKey = instance.getAccessTokenKey();
		String accessTokenValue = instance.getAccessTokenValue(accessTokenKey);
		System.out.println("accessTokenKey:"+accessTokenKey+", " + "accessTokenValue:"+accessTokenValue);
		
	}
	
	
	
}
