package com.garlicts.framework.distributed.redis;

import com.garlicts.framework.FrameworkConstant;
import com.garlicts.framework.config.PropertiesProvider;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisTemplate {

	private JedisTemplate() {
		
	}

	private static class JedisInit {
		
		private static JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), PropertiesProvider.getString(FrameworkConstant.REDIS_SERVER));

	}
	
	public static JedisPool getJedisPool(){
		return JedisInit.jedisPool;
	}
	
	public static Jedis getJedis(){
		Jedis jedis = JedisInit.jedisPool.getResource();
		return jedis;
	}
	
	public static void closeJedisPool(){
		getJedisPool().close();
	}
	
	public static void main(String[] args) {
		
		JedisPool jedisPool1 = JedisTemplate.getJedisPool();
		JedisPool jedisPool2 = JedisTemplate.getJedisPool();
		System.out.println(jedisPool1.hashCode() == jedisPool2.hashCode());
		
		Jedis jedis1 = JedisTemplate.getJedis();
		Jedis jedis2 = JedisTemplate.getJedis();
		System.out.println(jedis1.hashCode() == jedis2.hashCode());
		
	}

}
