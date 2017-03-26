package com.garlicts.framework.plugin.cache.redis;

import java.lang.reflect.Constructor;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.garlicts.framework.FrameworkConstant;
import com.garlicts.framework.config.PropertiesProvider;
import com.garlicts.framework.ioc.BeanContainerAbility;
import com.garlicts.framework.plugin.Plugin;
import com.garlicts.framework.util.ClassUtil;

public class RedisPlugin implements Plugin{
	
	JedisPool jedisPool;

	@Override
	public void init() {
		
		//读redis配置  格式为 192.168.8.100
		String redisServer = PropertiesProvider.getString(FrameworkConstant.REDIS_SERVER);
		jedisPool = new JedisPool(new JedisPoolConfig(), redisServer);

		//将RedisTemplate注册到Bean容器
		Class<?> redisTemplateClass = ClassUtil.loadClass("com.garlicts.framework.plugin.cache.redis.RedisTemplate");
		try {
			Constructor<?> constructor = redisTemplateClass.getConstructor(JedisPool.class);
			RedisTemplate redisTemplate = (RedisTemplate) constructor.newInstance(jedisPool);
			BeanContainerAbility.setBean(redisTemplateClass, redisTemplate);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
	}

	@Override
	public void destroy() {
		jedisPool.destroy();
	}

}
