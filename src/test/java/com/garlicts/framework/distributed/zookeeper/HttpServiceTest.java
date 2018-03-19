package com.garlicts.framework.distributed.zookeeper;

import org.junit.Test;

public class HttpServiceTest {

	@Test
	public void test1(){
		
		HttpServiceProvider httpServiceProvider = new HttpServiceProvider();
		httpServiceProvider.publish("test1", "test1", "127.0.0.1", 8080);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		HttpServiceConsumer httpServiceConsumer = new HttpServiceConsumer();
		String lookup = httpServiceConsumer.lookup("test1");
		System.out.println(lookup);
		
	}
	
}
