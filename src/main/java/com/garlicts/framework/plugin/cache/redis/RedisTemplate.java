package com.garlicts.framework.plugin.cache.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.util.SafeEncoder;

public class RedisTemplate {

	JedisPool jedisPool;

	public RedisTemplate(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	/**
	 * 更改key
	 * 
	 * @param String
	 *            oldkey
	 * @param String
	 *            newkey
	 * @return 状态码
	 * */
	public String rename(String oldkey, String newkey) {
		Jedis jedis = null;
		String rename = null;
		try {
			jedis = jedisPool.getResource();
			rename = jedis.rename(SafeEncoder.encode(oldkey), SafeEncoder.encode(newkey));
		} catch (Exception e) {

		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		return rename;

	}

	public String put(String key, String value) {
		Jedis jedis = null;
		String ret = null;
		try {
			jedis = jedisPool.getResource();
			ret = jedis.set(key, value);
		} catch (Exception e) {

		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		return ret;

	}

	public String put(String key, String value, int expireSeconds) {
		Jedis jedis = null;
		String ret = null;
		try {
			jedis = jedisPool.getResource();
			ret = jedis.setex(key, expireSeconds, value);
		} catch (Exception e) {

		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		return ret;

	}

	public String get(String key) {
		Jedis jedis = null;
		String value = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {

		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		return value;

	}
	
	public Long delete(String... keys) {
		Jedis jedis = null;
		Long ret = null;
		try {
			jedis = jedisPool.getResource();
			ret = jedis.del(keys);
		} catch (Exception e) {

		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		return ret;

	}	
	
	public Long increase(String key) {
		Jedis jedis = null;
		Long ret = null;
		try {
			jedis = jedisPool.getResource();
			ret = jedis.incr(key);
		} catch (Exception e) {

		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		return ret;

	}	

}
