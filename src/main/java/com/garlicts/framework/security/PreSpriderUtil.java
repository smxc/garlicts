package com.garlicts.framework.security;

import com.garlicts.framework.distributed.redis.JedisTemplate;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;

/**
 * Created by yanzz on 2017/4/10. 防止爬虫的代码
 */
public class PreSpriderUtil {

	private static final int VALIDATETIME = 500;
	private static final int COUNT = 3;

	// 防止过滤爬虫
	/**
	 * 对于同一个ip具体实现 获取当前请求的ip的值，根据键值去获取redis缓存数据苦中取出 该值，若无则添加进redis中说明该请求第一次不做判断
	 * 若有值去除上词请求的时间 和本次的的请求时间进行相减 若小于0.5s则 添加1 如果计数器达到 3次则表明这个是个可疑的ip 返回fasle
	 * 所有的数据都都在redis中 过滤爬虫
	 * 
	 * @return
	 */
	public boolean filterSprider(HttpServletRequest request) {
		/**
		 * 获取客户端的ip的地址
		 */
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		/**
		 * ip 地址为键值在redis中获取值
		 */
		JedisPool jedisPool = JedisTemplate.getJedisPool();
		Jedis jedis = jedisPool.getResource();
		String value = jedis.get(ip);
		/**
		 * 说明是第一次进入该方法
		 */
		String dateLong = String.valueOf(new Date().getTime());
		if (value == null) {
			jedis.set(ip, dateLong);
		} else {
			String upDateLong = jedis.get(ip);
			long s = Long.valueOf(dateLong) - Long.valueOf(upDateLong);
			// 则为可疑情况
			if (s <= VALIDATETIME) {
				/**
				 * 获取可疑的次数
				 */
				String count = jedis.get(ip + "count");
				if (count == null) {
					/**
					 * 说明是第一次出现这个问题
					 */
					jedis.set(ip + "count", dateLong);
				} else if (Integer.parseInt(count) >= COUNT) {
					/**
					 * 说明可能是一个爬虫 返回false 向前台发送验证码
					 */
					return false;
				} else {
					int newCount = Integer.parseInt(count) + 1;
					/**
					 * 存放于redis中
					 */
					jedis.set(ip + "count", newCount + "");
				}
			}

		}

		return true;
		
	}

}
